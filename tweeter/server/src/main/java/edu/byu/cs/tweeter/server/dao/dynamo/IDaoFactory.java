package edu.byu.cs.tweeter.server.dao.dynamo;

public interface IDaoFactory {
    IUserDao getUserDao();
    IAuthTokenDao getAuthTokenDao();
    IFollowDao getFollowDao();
    IStoryDao getStoryDao();
    IFeedDao getFeedDao();
}
