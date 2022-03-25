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
}
