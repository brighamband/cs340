package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;

public class FeedDao implements IFeedDao {

    Table feedTable;

    public FeedDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        feedTable = dynamoDB.getTable("Feed");
    }

    @Override
    public boolean create(String viewerAlias, long timestamp, String post, String authorAlias) {
        try {
            System.out.println("Adding a new status to feed table...");
            Item itemToPut = new Item()
                    .withPrimaryKey("viewerAlias", viewerAlias, "timestamp", timestamp)
                    .withString("post", post)
                    .withString("authorAlias", authorAlias);
            feedTable.putItem(itemToPut);

            System.out.println("Successfully added status to feed table.");

            return true;
        }
        catch (Exception e) {
            System.err.println("Unable to add status to feed table");
            System.err.println(e.getMessage());
            return false;
        }
    }

//    @Override
//    public List<Status> get(String username, int limit, Status lastStatus) {
//        return null;
//    }
}
