package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class AuthTokenDao implements IAuthTokenDao {

    Table authTokenTable;


    public AuthTokenDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        authTokenTable = dynamoDB.getTable("AuthToken");
    }

    @Override
    public AuthToken create(String alias) {
        // Make new token and timestamp for current time
        String token = UUID.randomUUID().toString();
        GregorianCalendar calendar = new GregorianCalendar();
        long timestamp = calendar.getTimeInMillis();

        try {
            // Add item to db
            System.out.println("Adding a new auth token...");
            Item itemToPut = new Item()
                    .withPrimaryKey("token", token)
                    .withString("alias", alias)
                    .withLong("timestamp", timestamp);
            authTokenTable.putItem(itemToPut);

            System.out.println("Successfully added token.");

            // Convert timestamp to datetime
            String datetime = calendar.getTime().toString();

            // Return new auth token
            AuthToken newAuthToken = new AuthToken(token, datetime);
            System.out.println("Auth token: " + newAuthToken);
            return newAuthToken;
        }
        catch (Exception e) {
            System.err.println("Unable to add auth token for user with alias of: " + alias);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public long getTimestamp(String token) {
        try {
            // Get item
            System.out.println("Getting auth token for " + token);
            Item item = authTokenTable.getItem("token", token);

            System.out.println("Item: " + item);
            if (item == null) return -1;

            long timestamp = item.getLong("timestamp");
//            AuthToken foundAuthToken = new AuthToken(
//                    item.getString("token"),
//                    longTimestampToString(item.getLong("timestamp"))
//            );
//            System.out.println("Auth token found: " + foundAuthToken);
            System.out.println("Auth token found with timestamp of " + timestamp);
            return timestamp;
        }
        catch (Exception e) {
            System.err.println("Unable to get auth token for " + token);
            System.err.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Updates expiration of auth token to current time.
     */
    @Override
    public void renewToken(String token) {
        // Make new timestamp for current time
        GregorianCalendar calendar = new GregorianCalendar();
        long timestamp = calendar.getTimeInMillis();

        // Make update spec
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("token", token)
                .withUpdateExpression("set timestamp = :ts")
                .withValueMap(new ValueMap().withLong(":ts", timestamp))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            // Update item
            System.out.println("Renewing auth token for " + token);
            UpdateItemOutcome outcome = authTokenTable.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to renew auth token for " + token);
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void remove(String token) {
        // Make delete spec
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey("token", token)
                .withConditionExpression("token = :token")
                .withValueMap(new ValueMap().withString(":token", token));

        try {
            // Update item
            System.out.println("Removing auth token for " + token);
            authTokenTable.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded:");
        }
        catch (Exception e) {
            System.err.println("Unable to renew auth token for " + token);
            System.err.println(e.getMessage());
        }
    }


    /**
     * Convert a long representing a time stamp to its string representation.
     */
    private static String longTimestampToString(long timestamp) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timestamp);
        return calendar.getTime().toString();
    }
}
