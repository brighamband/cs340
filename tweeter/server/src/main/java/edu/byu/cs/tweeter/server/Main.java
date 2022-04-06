package edu.byu.cs.tweeter.server;

import java.io.FileWriter;
import java.io.IOException;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.server.dao.dynamo.FollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.UserDao;

public class Main {
    public static void main(String[] args) throws IOException {
        // Set up log file
        FileWriter fw = null;
        try {
            // Set up file to write to
            String strPath = System.getProperty("user.dir") + "/server/src/main/java/edu/byu/cs/tweeter/server/out.txt";
            fw = new FileWriter(strPath, true);

            // Start process
            System.out.println("Starting...");

            /**
             * Create main user (@user0)
             */

            // Make user0 -- the one with all the followers
//        RegisterRequest registerRequest = new RegisterRequest(
//                "User",
//                "0",
//                "@user0",
//                "password",
//                "DEFAULT"
//        );
//        RegisterResponse registerResponse = new RegisterHandler().handleRequest(registerRequest, null);

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

        } catch(IOException e) {
            System.err.println(e);
        } finally {
            if (fw != null) {
                fw.close();
            }
        }

        System.out.println("All done!");
    }


//            LoginRequest loginRequest = new LoginRequest(
//                    follower.getAlias(),
//                    followerPassword
//            );
//            LoginResponse loginResponse = new LoginHandler().handleRequest(loginRequest, null);
//
//            if (loginResponse.isSuccess()) {
//                followerAuthToken = loginResponse.getAuthToken();
//                System.out.println("Logged in " + loginResponse.getUser().getAlias());
//            } else {            // Otherwise register
//                RegisterRequest registerRequest = new RegisterRequest(
//                        follower.getFirstName(),
//                        follower.getLastName(),
//                        follower.getAlias(),
//                        followerPassword,
//                        follower.getImageUrl());
//                RegisterResponse registerResponse = new RegisterHandler().handleRequest(registerRequest, null);
//                followerAuthToken = registerResponse.getAuthToken();
//                System.out.println("Registered " + registerResponse.getUser().getAlias());
//            }

//            FollowRequest followRequest = new FollowRequest(
//                    followerAuthToken,
//                    new User(
//                        "User",
//                            "0",
//                            "@user0",
//                            "DEFAULT"
//                    )
//            );
//            Response followResponse = new FollowHandler().handleRequest(followRequest, null);
//            if (followResponse.isSuccess()) {
//                System.out.println(follower.getAlias() + " now follows @user0");
//            } else {
//                System.out.println(follower.getAlias() + " was already following @user0");
//            }
}
