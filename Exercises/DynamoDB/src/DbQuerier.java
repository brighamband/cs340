import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.Map;

public class DbQuerier {
    public static void main(String[] args) {
        final int PAGE_SIZE = 10;

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("follows");

        // Query the “follows” table without pagination
        queryAllUsersFollowedByUser(table, "@follower");

        // Query the “follows_index” index without pagination
        queryAllUsersFollowingUser(table, "@followee");

        // Query the “follows” table with pagination
        queryUsersFollowedByUserPaginated(table, "@follower", PAGE_SIZE);

        // Query the “follows_index” index with pagination
        queryUsersFollowingUserPaginated(table, "@followee", PAGE_SIZE);
    }

    /**
     * Queries the “follows” table to return all the users being followed by a user, sorted by “followee_handle”.
     * Results are not paginated.
     */
    private static void queryAllUsersFollowedByUser(Table table, String followerHandle) {
        int numResults = 0;

        QuerySpec querySpec = new QuerySpec()
                .withHashKey("follower_handle", followerHandle)
                .withScanIndexForward(true);    // Sort ascending

        ItemCollection<QueryOutcome> items;

        try {
            System.out.println("Results for query of users being followed by " + followerHandle + ":");
            items = table.query(querySpec);

            for (Item item : items) {
                System.out.println(item);
                numResults++;
            }

            System.out.println("End of results (" + numResults + ")");
        }
        catch (Exception e) {
            System.err.println("Unable to query users being followed by " + followerHandle + ":");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Queries the “follows_index” index to return all the users following a user, reverse sorted by “follower_handle”.
     * Results are not paginated.
     */
    private static void queryAllUsersFollowingUser(Table table, String followeeHandle) {
        int numResults = 0;

        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followee_handle", followeeHandle)
                .withScanIndexForward(false);   // Sort descending

        ItemCollection<QueryOutcome> items;

        try {
            System.out.println("Results for query of users following " + followeeHandle + ":");
            items = table.getIndex("follows_index").query(querySpec);

            for (Item item : items) {
                System.out.println(item);
                numResults++;
            }

            System.out.println("End of results (" + numResults + ")");
        }
        catch (Exception e) {
            System.err.println("Unable to query users following " + followeeHandle);
            System.err.println(e.getMessage());
        }
    }

    private static void queryUsersFollowedByUserPaginated(Table table, String followerHandle, int pageSize) {
        int pageNumber = 1;
        int numResults = 0;

        System.out.println("Results for query of users being followed by " + followerHandle + " (paginated):");

        QuerySpec querySpec = new QuerySpec()
                .withHashKey("follower_handle", followerHandle)
                .withScanIndexForward(true) // Sort ascending
                .withMaxResultSize(pageSize);

        Map<String, AttributeValue> lastKeyMap = null;

        do {
            ItemCollection<QueryOutcome> items = null;

            if (lastKeyMap != null) {
                querySpec.withExclusiveStartKey("follower_handle", followerHandle, "followee_handle", lastKeyMap.get("followee_handle").getS());
            }

            try {
                items = table.query(querySpec);

                System.out.println("Page #" + pageNumber);
                for (Item item : items) {
                    System.out.println(item);
                    numResults++;
                }

                lastKeyMap = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();
                if (lastKeyMap == null) {
                    System.out.println("End of results (" + numResults + ")");
                }

            } catch (Exception e) {
                System.err.println("Unable to query users being followed by " + followerHandle + ":");
                System.err.println(e.getMessage());
                return;
            }

            pageNumber++;

        } while (lastKeyMap != null);
    }

    private static void queryUsersFollowingUserPaginated(Table table, String followeeHandle, int pageSize) {
        int pageNumber = 1;
        int numResults = 0;

        System.out.println("Results for query of users being followed by " + followeeHandle + " (paginated):");

        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followee_handle", followeeHandle)
                .withScanIndexForward(false)   // Sort descending
                .withMaxResultSize(pageSize);

        Map<String, AttributeValue> lastKeyMap = null;

        do {
            ItemCollection<QueryOutcome> items = null;

            if (lastKeyMap != null) {
                querySpec.withExclusiveStartKey("follower_handle", lastKeyMap.get("follower_handle").getS(), "followee_handle", followeeHandle);
            }

            try {
                items = table.getIndex("follows_index").query(querySpec);

                System.out.println("Page #" + pageNumber);
                for (Item item : items) {
                    System.out.println(item);
                    numResults++;
                }

                lastKeyMap = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();
                if (lastKeyMap == null) {
                    System.out.println("End of results (" + numResults + ")");
                }

            } catch (Exception e) {
                System.err.println("Unable to query users being followed by " + followeeHandle + ":");
                System.err.println(e.getMessage());
                return;
            }

            pageNumber++;

        } while (lastKeyMap != null);
    }
}
