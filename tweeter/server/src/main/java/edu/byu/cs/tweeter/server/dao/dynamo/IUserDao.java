package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.User;

public interface IUserDao {
    User create(String firstName, String lastName, String alias, String hashedPassword, String imageUrl); // Register
    User getUser(String alias);  // GetUser
    String getHashedPassword(String alias); // For Login
    int getFollowingCount(String alias);
    boolean setFollowingCount(String alias, int followingCount);
    int getFollowersCount(String alias);
    boolean setFollowersCount(String alias, int followersCount);
}
