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

        Table table = dynamoDB.getTable("Follows");

        // Query the “follows” table without pagination
        queryAllUsersFollowedByUser(table, "@follower");

        // Query the “FollowsIndex” index without pagination
        queryAllUsersFollowingUser(table, "@followee");

        // Query the “follows” table with pagination
        queryUsersFollowedByUserPaginated(table, "@follower", PAGE_SIZE);

        // Query the “FollowsIndex” index with pagination
        queryUsersFollowingUserPaginated(table, "@followee", PAGE_SIZE);
    }

    /**
     * Queries the “follows” table to return all the users being followed by a user, sorted by “followeeAlias”.
     * Results are not paginated.
     */
    private static void queryAllUsersFollowedByUser(Table table, String followerAlias) {
        int numResults = 0;

        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followerAlias", followerAlias)
                .withScanIndexForward(true);    // Sort ascending

        ItemCollection<QueryOutcome> items;

        try {
            System.out.println("Results for query of users being followed by " + followerAlias + ":");
            items = table.query(querySpec);

            for (Item item : items) {
                System.out.println(item);
                numResults++;
            }

            System.out.println("End of results (" + numResults + ")");
        }
        catch (Exception e) {
            System.err.println("Unable to query users being followed by " + followerAlias + ":");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Queries the “FollowsIndex” index to return all the users following a user, reverse sorted by “followerAlias”.
     * Results are not paginated.
     */
    private static void queryAllUsersFollowingUser(Table table, String followeeAlias) {
        int numResults = 0;

        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followeeAlias", followeeAlias)
                .withScanIndexForward(false);   // Sort descending

        ItemCollection<QueryOutcome> items;

        try {
            System.out.println("Results for query of users following " + followeeAlias + ":");
            items = table.getIndex("FollowsIndex").query(querySpec);

            for (Item item : items) {
                System.out.println(item);
                numResults++;
            }

            System.out.println("End of results (" + numResults + ")");
        }
        catch (Exception e) {
            System.err.println("Unable to query users following " + followeeAlias);
            System.err.println(e.getMessage());
        }
    }

    private static void queryUsersFollowedByUserPaginated(Table table, String followerAlias, int pageSize) {
        int pageNumber = 1;
        int numResults = 0;

        System.out.println("Results for query of users being followed by " + followerAlias + " (paginated):");

        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followerAlias", followerAlias)
                .withScanIndexForward(true) // Sort ascending
                .withMaxResultSize(pageSize);

        Map<String, AttributeValue> lastKeyMap = null;

        do {
            ItemCollection<QueryOutcome> items = null;

            if (lastKeyMap != null) {
                querySpec.withExclusiveStartKey("followerAlias", followerAlias, "followeeAlias", lastKeyMap.get("followeeAlias").getS());
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
                System.err.println("Unable to query users being followed by " + followerAlias + ":");
                System.err.println(e.getMessage());
                return;
            }

            pageNumber++;

        } while (lastKeyMap != null);
    }

    private static void queryUsersFollowingUserPaginated(Table table, String followeeAlias, int pageSize) {
        int pageNumber = 1;
        int numResults = 0;

        System.out.println("Results for query of users being followed by " + followeeAlias + " (paginated):");

        QuerySpec querySpec = new QuerySpec()
                .withHashKey("followeeAlias", followeeAlias)
                .withScanIndexForward(false)   // Sort descending
                .withMaxResultSize(pageSize);

        Map<String, AttributeValue> lastKeyMap = null;

        do {
            ItemCollection<QueryOutcome> items = null;

            if (lastKeyMap != null) {
                querySpec.withExclusiveStartKey("followerAlias", lastKeyMap.get("followerAlias").getS(), "followeeAlias", followeeAlias);
            }

            try {
                items = table.getIndex("FollowsIndex").query(querySpec);

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
                System.err.println("Unable to query users being followed by " + followeeAlias + ":");
                System.err.println(e.getMessage());
                return;
            }

            pageNumber++;

        } while (lastKeyMap != null);
    }
}
