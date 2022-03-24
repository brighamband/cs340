package edu.byu.cs.tweeter.server.dao.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

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

        try {
            System.out.println("Adding a new user...");
            Item itemToPut = new Item()
                    .withPrimaryKey("alias", alias)
                    .withString("firstName", firstName)
                    .withString("lastName", lastName)
                    .withString("hashedPassword", hashedPassword)
                    .withString("imageUrl", imageUrl)
                    .withNumber("followingCount", 0)
                    .withNumber("followersCount", 0);
            userTable.putItem(itemToPut);

            System.out.println("Successfully added user.");

            User newUser = new User(firstName, lastName, alias, imageUrl);
            System.out.println("User: " + newUser);
            return newUser;
        }
        catch (Exception e) {
            System.err.println("Unable to add user with alias of: " + alias);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User getUser(String alias) {
        try {
            System.out.println("Getting user with alias of " + alias);
            Item item = userTable.getItem("alias", alias);

            System.out.println("Item: " + item);
            if (item == null) return null;
            User foundUser = new User(
                item.getString("firstName"),
                item.getString("lastName"),
                item.getString("alias"),
                item.getString("imageUrl")
            );
            System.out.println("User found: " + foundUser);

            return foundUser;
        }
        catch (Exception e) {
            System.err.println("Unable to get user with alias of: " + alias);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public String getHashedPassword(String alias) {
        try {
            System.out.println("Finding hashed password for " + alias);
            Item item = userTable.getItem("alias", alias);

            System.out.println("Item: " + item);
            if (item == null) return null;
            String hashedPassword = item.getString("hashedPassword");
            System.out.println("Hashed password for " + alias  + ": " + hashedPassword);

            return hashedPassword;
        }
        catch (Exception e) {
            System.err.println("Unable to get hashed password for " + alias);
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param alias the alias of the User whose count of how many following is desired.
     * @return said count.
     */
    @Override
    public int getFollowingCount(String alias) {
        try {
            System.out.println("Finding following count for " + alias);
            Item item = userTable.getItem("alias", alias);

            System.out.println("Item: " + item);
            if (item == null) return -1;
            int followingCount = item.getInt("followingCount");
            System.out.println("Following count for " + alias  + ": " + followingCount);

            return followingCount;
        }
        catch (Exception e) {
            System.err.println("Unable to get following count for " + alias);
            System.err.println(e.getMessage());
            return -1;
        }
    }

    @Override
    public int getFollowersCount(String alias) {
        try {
            System.out.println("Finding followers count for " + alias);
            Item item = userTable.getItem("alias", alias);

            System.out.println("Item: " + item);
            if (item == null) return -1;
            int followersCount = item.getInt("followersCount");
            System.out.println("Followers count for " + alias  + ": " + followersCount);

            return followersCount;
        }
        catch (Exception e) {
            System.err.println("Unable to get followers count for " + alias);
            System.err.println(e.getMessage());
            return -1;
        }
    }
}
