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

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.TimeUtils;

public class AuthTokenDao implements IAuthTokenDao {

    Table authTokenTable;

    public final long TOKEN_TIME_TO_LIVE = 60;    // 60 seconds

    public AuthTokenDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        authTokenTable = dynamoDB.getTable("AuthToken");
    }

    @Override
    public AuthToken create(String alias) {
        // Make new token and expiration from current time
        String token = UUID.randomUUID().toString();
        long currTimestamp = TimeUtils.getCurrTimeAsLong();
        long expiration = calcExpiration(currTimestamp);

        try {
            // Add item to db
            System.out.println("Adding a new auth token...");
            Item itemToPut = new Item()
                    .withPrimaryKey("token", token)
                    .withString("alias", alias)
                    .withLong("expiration", expiration);
            authTokenTable.putItem(itemToPut);

            System.out.println("Successfully added token.");

            // Convert current time to datetime string
            String datetime = TimeUtils.longTimeToString(currTimestamp);

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

    public String getCurrUserAlias(String token) {
        try {
            // Get item
            System.out.println("Getting user who has auth token of " + token);
            Item item = authTokenTable.getItem("token", token);

            System.out.println("Item: " + item);
            if (item == null) return null;

            String alias = item.getString("alias");
            System.out.println("Alias of user with that token is " + alias);
            return alias;
        }
        catch (Exception e) {
            System.err.println("Unable to get auth token for " + token);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public long getExpiration(String token) {
        try {
            // Get item
            System.out.println("Getting auth token for " + token);
            Item item = authTokenTable.getItem("token", token);

            System.out.println("Item: " + item);
            if (item == null) return -1;

            long expiration = item.getLong("expiration");
            System.out.println("Auth token found with expiration of " + expiration);
            return expiration;
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
        long expiration = calcExpiration(TimeUtils.getCurrTimeAsLong());

        // Make update spec
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("token", token)
                .withUpdateExpression("set expiration = :exp")
                .withValueMap(new ValueMap().withLong(":exp", expiration))
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

    private long calcExpiration(long currTimestamp) {
        long expiration = currTimestamp + TOKEN_TIME_TO_LIVE;
        return expiration;
    }
}
