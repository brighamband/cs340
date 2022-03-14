import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;


public class DbAccessor {
    public static void main(String[] args) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("follows");

        final int NUM_ITEMS_TO_PUT = 25;

        // Put 25 items with same follower (@user1)
        for (int i = 1; i <= NUM_ITEMS_TO_PUT; i++) {
            String followerHandle = "@follower";
            String followerName = "Test Follower";
            String followeeHandle = "@user" + i;
            String followeeName = "User " + i;
            putItem(table, followerHandle, followerName, followeeHandle, followeeName);
        }

        // Put 25 items with same followee (@user2)
        for (int i = 1; i <= NUM_ITEMS_TO_PUT; i++) {
            String followerHandle = "@user" + i;
            String followerName = "User " + i;
            String followeeHandle = "@followee";
            String followeeName = "Test Followee";
            putItem(table, followerHandle, followerName, followeeHandle, followeeName);
        }

        // Get item by primary key ("follower_handle")
        getItem(table, "@follower", "@user122");

        // Update follower_name and followee_name of an item (@follower -> @user1)
        updateItem(table, "@follower", "@user1",
                "Test Follower Updated", "Test Followee Updated");

        // Delete an item by primary key (@user22)
        deleteItem(table, "@user22", "@followee");
    }

    private static void putItem(Table table, String followerHandle, String followerName,
                                String followeeHandle, String followeeName) {
        try {
            System.out.println("Adding a new item...");
            Item itemToPut = new Item().withPrimaryKey(
                    "follower_handle", followerHandle,
                            "followee_handle", followeeHandle)
                    .withString("follower_name", followerName)
                    .withString("followee_name", followeeName);
            PutItemOutcome outcome = table.putItem(itemToPut);

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        }
        catch (Exception e) {
            System.err.println("Unable to add item with follower_handle of: " + followerHandle);
            System.err.println(e.getMessage());
        }
    }

    public static void getItem(Table table, String followerHandle, String followeeHandle) {
        try {
            System.out.println("Attempting to read item with follower handle " + followerHandle +
                    " and followee handle of " + followeeHandle);
            Item outcome = table.getItem("follower_handle", followerHandle,
                    "followee_handle", followeeHandle);
            if (outcome == null) {
                throw new Exception("Item not found");
            }
            System.out.println("GetItem succeeded: " + outcome);
        }
        catch (Exception e) {
            System.err.println("Unable to read item with follower_handle of: " + followerHandle);
            System.err.println(e.getMessage());
        }
    }

    public static void updateItem(Table table, String followerHandle, String followeeHandle,
                                  String newFollowerName, String newFolloweeName) {
        PrimaryKey pk = new PrimaryKey("follower_handle", followerHandle,
                "followee_handle", followeeHandle);
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey(pk)
                .withUpdateExpression("set follower_name = :fr, followee_name = :fe")
                .withValueMap(new ValueMap().withString(":fr", newFollowerName).withString(":fe", newFolloweeName))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update item with follower_handle of: " + followerHandle);
            System.err.println(e.getMessage());
        }
    }

    public static void deleteItem(Table table, String followerHandle, String followeeHandle) {
        PrimaryKey pk = new PrimaryKey("follower_handle", followerHandle,
                "followee_handle", followeeHandle);
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(pk)
                .withConditionExpression("follower_handle = :val")
                .withValueMap(new ValueMap().withString(":val", followerHandle));

        try {
            System.out.println("Attempting a conditional delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete item with follower_handle of: " + followerHandle);
            System.err.println(e.getMessage());
        }
    }

}
