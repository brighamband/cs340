package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.GregorianCalendar;

public class DynamoDaoFactory implements IDaoFactory {
    @Override
    public IUserDao getUserDao() {
        return new UserDao();
    }

    @Override
    public IAuthTokenDao getAuthTokenDao() {
        return new AuthTokenDao();
    }

    @Override
    public IFollowDao getFollowDao() {
        return new FollowDao();
    }

    @Override
    public IStoryDao getStoryDao() {
        return new StoryDao();
    }

    @Override
    public IFeedDao getFeedDao() {
        return new FeedDao();
    }
}
