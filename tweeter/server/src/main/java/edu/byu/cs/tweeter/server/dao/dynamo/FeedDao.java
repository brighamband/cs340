package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.TimeUtils;
import edu.byu.cs.tweeter.util.Pair;

public class FeedDao implements IFeedDao {

    Table feedTable;

    public FeedDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        feedTable = dynamoDB.getTable("Feed");
    }

    @Override
    public boolean create(String viewerAlias, long timestamp, String post, String authorAlias) {
        final String msg = "status to feed table";

        try {
            Item itemToPut = new Item()
                    .withPrimaryKey("viewerAlias", viewerAlias, "timestamp", timestamp)
                    .withString("post", post)
                    .withString("authorAlias", authorAlias);
            feedTable.putItem(itemToPut);

            // System.out.println("Successfully added " + msg);

            return true;
        } catch (Exception e) {
            System.err.println("Unable to add " + msg);
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Pair<List<Status>, Boolean> getFeed(String viewerAlias, int limit, Long lastTimestamp) {
        List<Status> feed = new ArrayList<>();
        boolean hasMorePages = false;

        final String msg = "query of getting feed of " + viewerAlias;

        // Set up query
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("viewerAlias", viewerAlias)
                .withScanIndexForward(false) // Sort most recent posts first
                .withMaxResultSize(limit);
        // Have query start from lastTimestamp if there was one, otherwise go from
        // beginning
        if (lastTimestamp != null) {
            querySpec.withExclusiveStartKey("viewerAlias", viewerAlias, "timestamp", lastTimestamp);
        }

        try {
            ItemCollection<QueryOutcome> items = feedTable.query(querySpec);

            for (Item item : items) {
                Status statusToAdd = new Status(
                        item.getString("post"),
                        new User( // The null data will get filled in later
                                null,
                                null,
                                item.getString("authorAlias"),
                                null),
                        TimeUtils.longTimeToString(item.getLong("timestamp")));
                feed.add(statusToAdd);
            }

            // Check to see if there's more data to be retrieved
            Map<String, AttributeValue> lastKeyMap = items.getLastLowLevelResult().getQueryResult()
                    .getLastEvaluatedKey();
            if (lastKeyMap != null) {
                hasMorePages = true;
            }

            System.out.println("Successfully made " + msg);

        } catch (Exception e) {
            System.err.println("Unable to make " + msg);
            System.err.println(e.getMessage());
            return new Pair<>(null, null); // Error state
        }

        return new Pair<>(feed, hasMorePages);
    }
}
