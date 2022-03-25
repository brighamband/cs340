package edu.byu.cs.tweeter.server.dao.dynamo;

public interface IFollowDao {
    boolean create(String followerAlias, String followeeAlias);
    boolean remove(String followerAlias, String followeeAlias);
    Boolean isFollower(String followerAlias, String followeeAlias);
}
