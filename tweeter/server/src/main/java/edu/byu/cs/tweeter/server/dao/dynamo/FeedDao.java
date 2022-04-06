package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.UpdateFeedsMsg;
import edu.byu.cs.tweeter.server.TimeUtils;
import edu.byu.cs.tweeter.util.Pair;

public class FeedDao implements IFeedDao {

    private String TABLE_NAME = "Feed";

    DynamoDB dynamoDB;
    Table feedTable;

    public FeedDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        dynamoDB = new DynamoDB(client);

        feedTable = dynamoDB.getTable(TABLE_NAME);
    }

    @Override
    public void batchCreate(UpdateFeedsMsg msg) {

        // Constructor for TableWriteItems takes the name of the table, which I have
        // stored in TABLE_USER
        TableWriteItems items = new TableWriteItems(TABLE_NAME);

        // Add each follower into the TableWriteItems object
        for (String followerAlias : msg.getFollowerAliases()) {

            Item item = new Item()
                    .withPrimaryKey("viewerAlias", followerAlias, "timestamp", msg.getTimestamp())
                    .withString("post", msg.getPost())
                    .withString("authorAlias", msg.getAuthorAlias());
            items.addItemToPut(item);

            // 25 is the maximum number of items allowed in a single batch write.
            // Attempting to write more than 25 items will result in an exception being
            // thrown
            if (items.getItemsToPut() != null && items.getItemsToPut().size() == 25) {
                loopBatchWrite(items);
                items = new TableWriteItems(TABLE_NAME);
            }
        }

        // Write any leftover items
        if (items.getItemsToPut() != null && items.getItemsToPut().size() > 0) {
            loopBatchWrite(items);
        }
    }

    private void loopBatchWrite(TableWriteItems items) {

        // The 'dynamoDB' object is of type DynamoDB and is declared statically in this
        // example
        BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(items);

        // Check the outcome for items that didn't make it onto the table
        // If any were not added to the table, try again to write the batch
        while (outcome.getUnprocessedItems().size() > 0) {
            Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
            outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
        }
    }

    @Override
    public boolean create(String viewerAlias, String authorAlias, long timestamp, String post) {
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

            // System.out.println("Successfully made " + msg);

        } catch (Exception e) {
            System.err.println("Unable to make " + msg);
            System.err.println(e.getMessage());
            return new Pair<>(null, null); // Error state
        }

        return new Pair<>(feed, hasMorePages);
    }
}
