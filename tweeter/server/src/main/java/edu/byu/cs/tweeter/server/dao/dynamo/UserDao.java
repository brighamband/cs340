package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import edu.byu.cs.tweeter.model.domain.User;

public class UserDao implements IUserDao {

    Table userTable;

    public UserDao() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion("us-east-2").build();

        DynamoDB dynamoDB = new DynamoDB(client);

        userTable = dynamoDB.getTable("User");
    }

    @Override
    public User create(String firstName, String lastName, String alias, String hashedPassword, String imageUrl) {
        final String msg = "user with alias of: " + alias;

        try {
            Item itemToPut = new Item()
                    .withPrimaryKey("alias", alias)
                    .withString("firstName", firstName)
                    .withString("lastName", lastName)
                    .withString("hashedPassword", hashedPassword)
                    .withString("imageUrl", imageUrl)
                    .withNumber("followingCount", 0)
                    .withNumber("followersCount", 0);
            userTable.putItem(itemToPut);

            User newUser = new User(firstName, lastName, alias, imageUrl);
            // System.out.println("Successfully added " + msg);
            return newUser;
        } catch (Exception e) {
            System.err.println("Unable to add " + msg);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User getUser(String alias) {
        final String msg = "user with alias of " + alias;

        try {
            Item item = userTable.getItem("alias", alias);
            if (item == null)
                return null;

            User foundUser = new User(
                    item.getString("firstName"),
                    item.getString("lastName"),
                    item.getString("alias"),
                    item.getString("imageUrl"));
            // System.out.println("Successfully found " + msg);
            return foundUser;
        } catch (Exception e) {
            System.err.println("Unable to find " + msg);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String getHashedPassword(String alias) {
        final String msg = "hashed password for " + alias;

        try {
            Item item = userTable.getItem("alias", alias);
            if (item == null)
                return null;

            String hashedPassword = item.getString("hashedPassword");
            // System.out.println("Successfully found " + msg);

            return hashedPassword;
        } catch (Exception e) {
            System.err.println("Unable to get " + msg);
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the count of users from the database that the user specified is
     * following. The
     * current implementation uses generated data and doesn't actually access a
     * database.
     *
     * @param alias the alias of the User whose count of how many following is
     *              desired.
     * @return said count.
     */
    @Override
    public int getFollowingCount(String alias) {
        final String msg = "following count for " + alias;

        try {
            Item item = userTable.getItem("alias", alias);
            if (item == null)
                return -1;

            int followingCount = item.getInt("followingCount");
            // System.out.println("Successfully got " + msg);

            return followingCount;
        } catch (Exception e) {
            System.err.println("Unable to get " + msg);
            System.err.println(e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean setFollowingCount(String alias, int followingCount) {
        final String msg = "following count for " + alias + " to be " + followingCount;

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("alias", alias)
                .withUpdateExpression("set followingCount = :fc")
                .withValueMap(new ValueMap().withInt(":fc", followingCount))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            userTable.updateItem(updateItemSpec);
            // System.out.println("Successfully set " + msg);
            return true;
        } catch (Exception e) {
            System.err.println("Unable to set " + msg);
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public int getFollowersCount(String alias) {
        final String msg = "followers count for " + alias;

        try {
            Item item = userTable.getItem("alias", alias);
            if (item == null)
                return -1;

            int followersCount = item.getInt("followersCount");
            // System.out.println("Successfully got " + msg);

            return followersCount;
        } catch (Exception e) {
            System.err.println("Unable to get " + msg);
            System.err.println(e.getMessage());
            return -1;
        }
    }

    @Override
    public boolean setFollowersCount(String alias, int followersCount) {
        final String msg = "followers count for " + alias + " to be " + followersCount;

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("alias", alias)
                .withUpdateExpression("set followersCount = :fc")
                .withValueMap(new ValueMap().withInt(":fc", followersCount))
                .withReturnValues(ReturnValue.UPDATED_NEW);

        try {
            userTable.updateItem(updateItemSpec);
            // System.out.println("Successfully set " + msg);
            return true;
        } catch (Exception e) {
            System.err.println("Unable to set " + msg);
            System.err.println(e.getMessage());
            return false;
        }
    }
}
