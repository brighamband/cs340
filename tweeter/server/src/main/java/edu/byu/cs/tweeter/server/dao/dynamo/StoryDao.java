package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

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


//    public Status createPost(String authorAlias, String post) {
//        long timestamp = TimeUtils.getCurrTimeInMs();
//
//
//        // Make status
//        Status newStatus = new Status(post, )
//    }
//
//    public List<Status> getStory() {
//
//    }
}
