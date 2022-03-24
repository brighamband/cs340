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
    public User create(String firstName, String lastName, String alias, String imageUrl) {

        // MAKE HASHED PASSWORD
        String hashedPassword = "FIXME";

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
            // FIXME -- Hard-coded
            return newUser;
        }
        catch (Exception e) {
            System.err.println("Unable to add user with alias of: " + alias);
            System.err.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public User getUser(String alias) {
        try {
            System.out.println("Finding user with alias of " + alias);
            Item item = userTable.getItem("alias", alias);

            System.out.println("Item: " + item);
            if (item == null) return null;
            User foundUser = new User(
                DynamoUtils.getStrAttrVal(item, "firstName"),
                DynamoUtils.getStrAttrVal(item, "lastName"),
                DynamoUtils.getStrAttrVal(item, "alias"),
                DynamoUtils.getStrAttrVal(item, "imageUrl")
            );
            System.out.println("Found user: " + foundUser);

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
            System.out.println("Finding user with alias of " + alias);
            Item item = userTable.getItem("alias", alias);

            System.out.println("Item: " + item);
            if (item == null) return null;
            String hashedPassword = DynamoUtils.getStrAttrVal(item, "hashedPassword");
            System.out.println("Hashed password for " + alias  + ": " + hashedPassword);

            return hashedPassword;
        }
        catch (Exception e) {
            System.err.println("Unable to get hashed password for " + alias);
            System.err.println(e.getMessage());
            return null;
        }
    }
}
