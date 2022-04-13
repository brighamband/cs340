package edu.byu.cs.tweeter.server;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.FollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.UserDao;
import edu.byu.cs.tweeter.server.lambda.RegisterHandler;
import edu.byu.cs.tweeter.server.service.StatusService;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting");

        IDaoFactory factory = new DynamoDaoFactory();
        StatusService service = new StatusService(factory);

        SQSEvent event = new SQSEvent();
        List<SQSEvent.SQSMessage> records = new ArrayList<>();
        SQSEvent.SQSMessage message = new SQSEvent.SQSMessage();
        message.setBody("{\"authorAlias\":\"@brighamband\",\"timestamp\":1649214925,\"post\":\"2\"}");;
        records.add(message);
        event.setRecords(records);

        service.postUpdateFeedMessages(event);
//        makePrimaryUser();
//        makeBunchOfFollowersForUser();

        System.out.println("All done!");
    }

    private static void makePrimaryUser() {
        // Make user0 -- the one with all the followers
        RegisterRequest registerRequest = new RegisterRequest(
                "User",
                "0",
                "@user0",
                "password",
                "DEFAULT"
        );
        RegisterResponse registerResponse = new RegisterHandler().handleRequest(registerRequest, null);
    }

    private static void makeBunchOfFollowersForUser() throws IOException {
        // Set up log file
        FileWriter fw = null;
        try {
            // Set up file to write to
            String strPath = System.getProperty("user.dir") + "/server/src/main/java/edu/byu/cs/tweeter/server/out.txt";
            fw = new FileWriter(strPath, true);

            // Start process
            System.out.println("Starting...");

            /**
             * Create 10,000 users and have them follow @user0
             */

            int START = 1;
            int END = 10000;

            // Make 10K users (user1-user10000)
            for (int i = START; i <= END; i++) {
                String resultMsg = "";

                // Part 1 - Make followers

                User follower = new User(
                        "User",
                        String.format("%d", i),
                        String.format("@user%d", i),
                        "DEFAULT"
                );
                String hashedPassword = "5f4dcc3b5aa765d61d8327deb882cf99";   // Hashed password for 'password'

                User newUser = new UserDao().create(
                        follower.getFirstName(), follower.getLastName(), follower.getAlias(), hashedPassword, follower.getImageUrl());
                if (newUser != null) {
                    resultMsg += follower.getAlias() + " created\n";
                } else {
                    resultMsg += "ERROR: " + follower.getAlias() + " could not be created\n";
                }


                // Part 2 - Have followers follow @user0

                boolean madeFollower = new FollowDao().create(
                        follower.getAlias(), "@user0");
                if (madeFollower) {
                    resultMsg += follower.getAlias() + " now follows @user0\n";
                } else {
                    resultMsg += "ERROR: " + follower.getAlias() + " was unable to follow @user0\n";
                }

                // Write message to out file
                fw.write(resultMsg);
            }

        } catch (IOException e) {
            System.err.println(e);
        } finally {
            if (fw != null) {
                fw.close();
            }
        }

        System.out.println("All done!");
    }
}

