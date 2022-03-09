import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Iterator;
import java.util.Map;

public class DbQuerier {
    public static void main(String[] args) {
        final int PAGE_SIZE = 10;

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("follows");


        // Query the “follows” table without pagination
        queryUsersFollowedByUser(table, "@follower");

        // Query the “follows_index” index without pagination
        queryUsersFollowingUser(table, "@followee");

        // Query the “follows” table with pagination
        Map<String, AttributeValue> lastKeyReturned = null;

//        do {
//
//            String followerHandle = "@follower";
//            QuerySpec querySpec = new QuerySpec()
//                    .withKeyConditionExpression("follower_handle = :name")
//                    .withValueMap(new ValueMap().withString(":name", followerHandle))
//                    .withScanIndexForward(true)    // Sort ascending
//                    .withMaxResultSize(PAGE_SIZE)   // Limit to 10 results
//                    .withExclusiveStartKey(lastKeyReturned.get(FIXME));
//
//            ItemCollection<QueryOutcome> items = null;
//
//            String queryDescription = "users followed by user";
//
//            try {
//                System.out.println("Results for query of " + queryDescription + ":");
//                items = table.query(querySpec);
//                lastKeyReturned = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();
//
//                Iterator<Item> iterator = items.iterator();
//                while (iterator.hasNext()) {
//                    Item item = iterator.next();
//                    System.out.println(item);
//                }
//            } catch (Exception e) {
//                System.err.println("Unable to query " + queryDescription);
//                System.err.println(e.getMessage());
//            }
//
//        } while (lastKeyReturned != null);

        // Query the “follows_index” index with pagination
    }

    /**
     * Queries the “follows” table to return all the users being followed by a user, sorted by “followee_handle”.
     * Results are not paginated.
     */
    private static void queryUsersFollowedByUser(Table table, String followerHandle) {
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("follower_handle = :name")
                .withValueMap(new ValueMap().withString(":name", followerHandle))
                .withScanIndexForward(true);    // Sort ascending

        runQuery(table, null, querySpec, "users being followed by " + followerHandle);
    }

    /**
     * Queries the “follows_index” index to return all the users following a user, reverse sorted by “follower_handle”.
     * Results are not paginated.
     */
    private static void queryUsersFollowingUser(Table table, String followeeHandle) {
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("followee_handle = :name")
                .withValueMap(new ValueMap().withString(":name", followeeHandle))
                .withScanIndexForward(false);   // Sort descending

        runQuery(table, "follows_index", querySpec, "users being followed by " + followeeHandle);
    }

    private static void runQuery(Table table, String indexName, QuerySpec querySpec, String queryDescription) {
        ItemCollection<QueryOutcome> items;

        try {
            System.out.println("Results for query of " + queryDescription + ":");
            if (indexName != null) {
                items = table.getIndex(indexName).query(querySpec);
            } else {
                items = table.query(querySpec);
            }

            for (Item item : items) {
                System.out.println(item);
            }
        }
        catch (Exception e) {
            System.err.println("Unable to query " + queryDescription);
            System.err.println(e.getMessage());
        }
    }
}
