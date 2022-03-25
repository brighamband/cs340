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

public class StoryDao implements IStoryDao {

    Table storyTable;

    public StoryDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        storyTable = dynamoDB.getTable("Story");
    }

    @Override
    public boolean create(String authorAlias, long timestamp, String post) {
        try {
            System.out.println("Adding a new status to story table...");
            Item itemToPut = new Item()
                    .withPrimaryKey("authorAlias", authorAlias, "timestamp", timestamp)
                    .withString("post", post);
            storyTable.putItem(itemToPut);

            System.out.println("Successfully added status to story table.");

            return true;
        }
        catch (Exception e) {
            System.err.println("Unable to add status to story table");
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Pair<List<Status>, Boolean> getStory(User user, int limit, Long lastTimestamp) {
        String authorAlias = user.getAlias();

        List<Status> story = new ArrayList<>();
        boolean hasMorePages = false;

        System.out.println("Results for query of getting story of " + authorAlias);

        // Set up query
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("authorAlias", authorAlias)
                .withScanIndexForward(true) // Sort ascending
                .withMaxResultSize(limit);
        // Have query start from lastTimestamp if there was one, otherwise go from beginning
        if (lastTimestamp != null) {
            querySpec.withExclusiveStartKey("authorAlias", authorAlias, "timestamp", lastTimestamp);
        }

        try {
            ItemCollection<QueryOutcome> items = storyTable.query(querySpec);

            for (Item item : items) {
                Status statusToAdd = new Status(
                        item.getString("post"),
                        user,
                        TimeUtils.longTimeToString(item.getLong("timestamp"))
                );
                story.add(statusToAdd);
                System.out.println(item);
            }

            // Check to see if there's more data to be retrieved
            Map<String, AttributeValue> lastKeyMap = items.getLastLowLevelResult().getQueryResult().getLastEvaluatedKey();
            if (lastKeyMap != null) {
                hasMorePages = true;
            }

        } catch (Exception e) {
            System.err.println("Unable to query/get story of " + authorAlias);
            System.err.println(e.getMessage());
            return new Pair<>(null, true);  // Error state
        }

        return new Pair<>(story, hasMorePages);
    }
}
