package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.TimeUtils;

public class AuthTokenDao implements IAuthTokenDao {

    Table authTokenTable;

    public final long TOKEN_TIME_TO_LIVE = 300; // 5 min

    public AuthTokenDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        authTokenTable = dynamoDB.getTable("AuthToken");
    }

    @Override
    public AuthToken create(String alias) {
        final String msg = "token for " + alias;

        // Make new token and expiration from current time
        String token = UUID.randomUUID().toString();
        long currTimestamp = TimeUtils.getCurrTimeAsLong();
        long expiration = calcExpiration(currTimestamp);

        try {
            // Add item to db
            Item itemToPut = new Item()
                    .withPrimaryKey("token", token)
                    .withString("alias", alias)
                    .withLong("expiration", expiration);
            authTokenTable.putItem(itemToPut);

            // System.out.println("Successfully created " + msg);

            // Convert current time to datetime string
            String datetime = TimeUtils.longTimeToString(currTimestamp);

            // Return new auth token
            AuthToken newAuthToken = new AuthToken(token, datetime);
            return newAuthToken;
        } catch (Exception e) {
            System.err.println("Unable to create " + msg);
            System.err.println(e.getMessage());
            return null;
        }
    }

    public String getCurrUserAlias(String token) {
        final String msg = "user alias for auth token (" + token + ")";

        try {
            // Get item
            Item item = authTokenTable.getItem("token", token);
            if (item == null)
                return null;

            String alias = item.getString("alias");
            // System.out.println("Successfully found " + msg);
            return alias;
        } catch (Exception e) {
            System.err.println("Unable to find " + msg);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public long getExpiration(String token) {
        final String msg = "expiration for auth token (" + token + ")";

        try {
            // Get item
            Item item = authTokenTable.getItem("token", token);
            if (item == null)
                return -1;

            long expiration = item.getLong("expiration");
            // System.out.println("Successfully found " + msg);
            return expiration;
        } catch (Exception e) {
            System.err.println("Unable to get " + msg);
            System.err.println(e.getMessage());
            return -1;
        }
    }

    /**
     * Updates expiration of auth token to current time.
     */
    @Override
    public void renewToken(String token) {
        final String msg = "auth token (" + token + ")";

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
            authTokenTable.updateItem(updateItemSpec);
            // System.out.println("Successfully updated/renewed " + msg);
        } catch (Exception e) {
            System.err.println("Unable to update/renew " + msg);
            System.err.println(e.getMessage());
        }
    }

    private long calcExpiration(long currTimestamp) {
        long expiration = currTimestamp + TOKEN_TIME_TO_LIVE;
        return expiration;
    }
}
