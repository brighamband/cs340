package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDao implements IFollowDao {

    Table followTable;

    public FollowDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        followTable = dynamoDB.getTable("Follow");
    }

    /**
     * Create item in table representing a follows relationship.
     */
    public boolean create(String followerAlias, String followeeAlias) {
        try {
            System.out.println("Adding a new follows relationship...");
            Item itemToPut = new Item()
                    .withPrimaryKey(
                    "followerAlias", followerAlias,
                    "followeeAlias", followeeAlias);
            PutItemOutcome outcome = followTable.putItem(itemToPut);

            System.out.println("PutItem for Follow Table succeeded:\n" + outcome.getPutItemResult());
            return true;
        }
        catch (Exception e) {
            System.err.println("Unable to add item with followerAlias of: " + followerAlias);
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Remove item in table that once represented a follows relationship (now unfollows).
     * @return
     */
    public boolean remove(String followerAlias, String followeeAlias) {

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey("followerAlias", followerAlias,
                        "followeeAlias", followeeAlias)
                .withConditionExpression("followerAlias = :val")
                .withValueMap(new ValueMap().withString(":val", followerAlias));

        try {
            System.out.println("Attempting to delete from Follow table");
            followTable.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem for Follow table succeeded");
            return true;
        }
        catch (Exception e) {
            System.err.println("Unable to delete item with followerAlias of: " + followerAlias);
            System.err.println(e.getMessage());
            return false;
        }
    }

    public Boolean isFollower(String followerAlias, String followeeAlias) {
        try {
            System.out.println("Checking if " + followerAlias + " is a follower of " + followeeAlias);
            Item item = followTable.getItem("followerAlias", followerAlias,
                    "followeeAlias", followeeAlias);
            if (item == null) {     // If not found
                return false;
            }
            System.out.println("True, " + followerAlias + " is a follower of " + followeeAlias);
            return true;
        }
        catch (Exception e) {
            System.err.println("Unable to check if " + followerAlias + " is a follower of " + followeeAlias);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public Pair<List<String>, Boolean> getFollowees(GetFollowingRequest request) {
        String followerAlias = request.getFollowerAlias();
        int limit = request.getLimit();
        String lastFolloweeAlias = request.getLastFolloweeAlias();

        List<String> followeeAliases = new ArrayList<>();
        boolean hasMorePages = false;

        System.out.println("Results for query of users being followed by " + followerAlias + " (paginated):");

        // Set up query
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followerAlias", followerAlias)
                .withScanIndexForward(true) // Sort ascending
                .withMaxResultSize(limit);
        // Have query start from lastFollowee if there was one, otherwise go from beginning
        if (lastFolloweeAlias != null) {
            querySpec.withExclusiveStartKey("followerAlias", followerAlias, "followeeAlias", lastFolloweeAlias);
        }

        try {
            ItemCollection<QueryOutcome>  items = followTable.query(querySpec);

            for (Item item : items) {
                followeeAliases.add(item.getString("followeeAlias"));
                System.out.println(item);
            }

            // Check to see if there's more data to be retrieved
            Map<String, AttributeValue> lastKeyMap = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();
            if (lastKeyMap != null) {
                hasMorePages = true;
            }

        } catch (Exception e) {
            System.err.println("Unable to query users being followed by " + followerAlias);
            System.err.println(e.getMessage());
            return new Pair<>(null, true);  // Error state
        }

        return new Pair<>(followeeAliases, hasMorePages);
    }

    public Pair<List<String>, Boolean> getFollowers(GetFollowersRequest request) {
        String followeeAlias = request.getFolloweeAlias();
        int limit = request.getLimit();
        String lastFollowerAlias = request.getLastFollowerAlias();

        List<String> followeeAliases = new ArrayList<>();
        boolean hasMorePages = false;

        System.out.println("Results for query of users following " + followeeAlias + " (paginated):");

        // Set up query
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followeeAlias", followeeAlias)
                .withScanIndexForward(true) // Sort ascending
                .withMaxResultSize(limit);
        // Have query start from lastFollowee if there was one, otherwise go from beginning
        if (lastFollowerAlias != null) {
            // FIXME -- The order here might be flipped between hashKey and sortKey
            querySpec.withExclusiveStartKey("followerAlias", lastFollowerAlias, "followeeAlias", followeeAlias);
        }

        try {
            ItemCollection<QueryOutcome>  items = followTable.getIndex("FollowIndex").query(querySpec);

            for (Item item : items) {
                followeeAliases.add(item.getString("followerAlias"));
                System.out.println(item);
            }

            // Check to see if there's more data to be retrieved
            Map<String, AttributeValue> lastKeyMap = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();
            if (lastKeyMap != null) {
                hasMorePages = true;
            }

        } catch (Exception e) {
            System.err.println("Unable to query users following " + followeeAlias);
            System.err.println(e.getMessage());
            return new Pair<>(null, true);  // Error state
        }

        return new Pair<>(followeeAliases, hasMorePages);
    }





    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }

    List<User> getDummyFollowers() {
        return getFakeData().getFakeUsers();
    }

//    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
//        // TODO: Generates dummy data. Replace with a real implementation.
//        assert request.getLimit() > 0;
//        assert request.getFolloweeAlias() != null;
//
//        List<User> allFollowers = getDummyFollowers();
//        List<User> responseFollowers = new ArrayList<>(request.getLimit());
//
//        boolean hasMorePages = false;
//
//        if(request.getLimit() > 0) {
//            if (allFollowers != null) {
//                int followersIndex = getFollowersStartingIndex(request.getLastFollowerAlias(), allFollowers);
//
//                for(int limitCounter = 0; followersIndex < allFollowers.size() && limitCounter < request.getLimit(); followersIndex++, limitCounter++) {
//                    responseFollowers.add(allFollowers.get(followersIndex));
//                }
//
//                hasMorePages = followersIndex < allFollowers.size();
//            }
//        }
//
//        return new GetFollowersResponse(responseFollowers, hasMorePages);
//    }

    private int getFollowersStartingIndex(String lastFollowerAlias, List<User> allFollowers) {

        int followersIndex = 0;

        if(lastFollowerAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowers.size(); i++) {
                if(lastFollowerAlias.equals(allFollowers.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followersIndex = i + 1;
                    break;
                }
            }
        }

        return followersIndex;
    }
}
