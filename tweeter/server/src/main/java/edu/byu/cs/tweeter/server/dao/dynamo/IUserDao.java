package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.User;

public interface IUserDao {
//    RegisterResponse register(RegisterRequest request);
//    User login(String username, String password);
//    GetUserResponse getUser(GetUserRequest request);
//    Response logout();

    User create(String firstName, String lastName, String alias, String imageUrl); // Register
    User login(String username, String password); // Login
    User getUser(String alias);  // GetUser
    String getHashedPassword(String alias); // For Login
    int getFollowingCount(String alias);
    int getFollowersCount(String alias);
}
