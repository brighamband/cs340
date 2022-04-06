package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
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
        final String msg = " with followerAlias of " + followerAlias;

        try {
            Item itemToPut = new Item()
                    .withPrimaryKey(
                            "followerAlias", followerAlias,
                            "followeeAlias", followeeAlias);
            followTable.putItem(itemToPut);

            // System.out.println("Successfully put item in Follow Table " + msg);
            return true;
        } catch (Exception e) {
            System.err.println("Unable to add item " + msg);
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Remove item in table that once represented a follows relationship (now
     * unfollows).
     * 
     * @return
     */
    public boolean remove(String followerAlias, String followeeAlias) {
        final String msg = "with followerAlias of " + followerAlias + " and followeeAlias of " + followeeAlias;

        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey("followerAlias", followerAlias,
                        "followeeAlias", followeeAlias)
                .withConditionExpression("followerAlias = :val")
                .withValueMap(new ValueMap().withString(":val", followerAlias));

        try {
            followTable.deleteItem(deleteItemSpec);
            System.out.println("Deleted item " + msg);
            return true;
        } catch (Exception e) {
            System.err.println("Unable to delete item " + msg);
            System.err.println(e.getMessage());
            return false;
        }
    }

    public Boolean isFollower(String followerAlias, String followeeAlias) {
        final String msg = followerAlias + " is a follower of " + followeeAlias;

        try {
            Item item = followTable.getItem("followerAlias", followerAlias,
                    "followeeAlias", followeeAlias);
            if (item == null) { // If not found
                return false;
            }
            // System.out.println("True, " + msg);
            return true;
        } catch (Exception e) {
            System.err.println("Unable to check if " + msg);
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

        final String msg = "query of users being followed by " + followerAlias;

        // Set up query
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followerAlias", followerAlias)
                .withScanIndexForward(true) // Sort ascending
                .withMaxResultSize(limit);
        // Have query start from lastFollowee if there was one, otherwise go from
        // beginning
        if (lastFolloweeAlias != null) {
            querySpec.withExclusiveStartKey("followerAlias", followerAlias, "followeeAlias", lastFolloweeAlias);
        }

        try {
            ItemCollection<QueryOutcome> items = followTable.query(querySpec);

            for (Item item : items) {
                followeeAliases.add(item.getString("followeeAlias"));
            }

            // Check to see if there's more data to be retrieved
            Map<String, AttributeValue> lastKeyMap = items.getLastLowLevelResult().getQueryResult()
                    .getLastEvaluatedKey();
            if (lastKeyMap != null) {
                hasMorePages = true;
            }

            // System.out.println("Successfully made " + msg);

        } catch (Exception e) {
            System.err.println("Unable to make " + msg);
            System.err.println(e.getMessage());
            return new Pair<>(null, null); // Error state
        }

        return new Pair<>(followeeAliases, hasMorePages);
    }

    public Pair<List<String>, Boolean> getFollowers(GetFollowersRequest request) {
        String followeeAlias = request.getFolloweeAlias();
        int limit = request.getLimit();
        String lastFollowerAlias = request.getLastFollowerAlias();

        List<String> followeeAliases = new ArrayList<>();
        boolean hasMorePages = false;

        final String msg = "query of users following " + followeeAlias;

        // Set up query
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followeeAlias", followeeAlias)
                .withScanIndexForward(true) // Sort ascending
                .withMaxResultSize(limit);
        // Have query start from lastFollowee if there was one, otherwise go from
        // beginning
        if (lastFollowerAlias != null) {
            querySpec.withExclusiveStartKey("followerAlias", lastFollowerAlias, "followeeAlias", followeeAlias);
        }

        try {
            ItemCollection<QueryOutcome> items = followTable.getIndex("FollowIndex").query(querySpec);

            for (Item item : items) {
                followeeAliases.add(item.getString("followerAlias"));
            }

            // Check to see if there's more data to be retrieved
            Map<String, AttributeValue> lastKeyMap = items.getLastLowLevelResult().getQueryResult()
                    .getLastEvaluatedKey();
            if (lastKeyMap != null) {
                hasMorePages = true;
            }

            // System.out.println("Successfully made " + msg);

        } catch (Exception e) {
            System.err.println("Unable to make " + msg);
            System.err.println(e.getMessage());
            return new Pair<>(null, null); // Error state
        }

        return new Pair<>(followeeAliases, hasMorePages);
    }
}
