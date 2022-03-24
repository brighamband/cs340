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

        Table table = dynamoDB.getTable("Follow");

        final int NUM_ITEMS_TO_PUT = 25;

        // Put 25 items with same follower (@user1)
        for (int i = 1; i <= NUM_ITEMS_TO_PUT; i++) {
            String followerAlias = "@follower";
            String followerName = "Test Follower";
            String followeeAlias = "@user" + i;
            String followeeName = "User " + i;
            putItem(table, followerAlias, followerName, followeeAlias, followeeName);
        }

        // Put 25 items with same followee (@user2)
        for (int i = 1; i <= NUM_ITEMS_TO_PUT; i++) {
            String followerAlias = "@user" + i;
            String followerName = "User " + i;
            String followeeAlias = "@followee";
            String followeeName = "Test Followee";
            putItem(table, followerAlias, followerName, followeeAlias, followeeName);
        }

        // Get item by primary key ("followerAlias")
        getItem(table, "@follower", "@user122");

        // Update followerName and followeeName of an item (@follower -> @user1)
        updateItem(table, "@follower", "@user1",
                "Test Follower Updated", "Test Followee Updated");

        // Delete an item by primary key (@user22)
        deleteItem(table, "@user22", "@followee");
    }

    private static void putItem(Table table, String followerAlias, String followerName,
                                String followeeAlias, String followeeName) {
        try {
            System.out.println("Adding a new item...");
            Item itemToPut = new Item().withPrimaryKey(
                    "followerAlias", followerAlias,
                            "followeeAlias", followeeAlias)
                    .withString("followerName", followerName)
                    .withString("followeeName", followeeName);
            PutItemOutcome outcome = table.putItem(itemToPut);

            System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());

        }
        catch (Exception e) {
            System.err.println("Unable to add item with followerAlias of: " + followerAlias);
            System.err.println(e.getMessage());
        }
    }

    public static void getItem(Table table, String followerAlias, String followeeAlias) {
        try {
            System.out.println("Attempting to read item with follower handle " + followerAlias +
                    " and followee handle of " + followeeAlias);
            Item outcome = table.getItem("followerAlias", followerAlias,
                    "followeeAlias", followeeAlias);
            if (outcome == null) {
                throw new Exception("Item not found");
            }
            System.out.println("GetItem succeeded: " + outcome);
        }
        catch (Exception e) {
            System.err.println("Unable to read item with followerAlias of: " + followerAlias);
            System.err.println(e.getMessage());
        }
    }

    public static void updateItem(Table table, String followerAlias, String followeeAlias,
                                  String newFollowerName, String newFolloweeName) {
        PrimaryKey pk = new PrimaryKey("followerAlias", followerAlias,
                "followeeAlias", followeeAlias);
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey(pk)
                .withUpdateExpression("set followerName = :fr, followeeName = :fe")
                .withValueMap(new ValueMap().withString(":fr", newFollowerName).withString(":fe", newFolloweeName))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update item with followerAlias of: " + followerAlias);
            System.err.println(e.getMessage());
        }
    }

    public static void deleteItem(Table table, String followerAlias, String followeeAlias) {
        PrimaryKey pk = new PrimaryKey("followerAlias", followerAlias,
                "followeeAlias", followeeAlias);
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(pk)
                .withConditionExpression("followerAlias = :val")
                .withValueMap(new ValueMap().withString(":val", followerAlias));

        try {
            System.out.println("Attempting a conditional delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete item with followerAlias of: " + followerAlias);
            System.err.println(e.getMessage());
        }
    }

}
