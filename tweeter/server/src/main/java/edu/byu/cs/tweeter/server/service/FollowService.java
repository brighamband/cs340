package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.dynamo.FollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {
    IDaoFactory factory;

    public FollowService(IDaoFactory factory) {
        this.factory = factory;
    }

    /**
     * Empty constructor just for mock tests.
     */
    public FollowService() {}

    FollowDao getFollowDaoTempFixme() {
        return new FollowDao();
    }
    /**
     * Returns the users that the user specified in the request is following. Uses information in
     * the request object to limit the number of followees returned and to return the next set of
     * followees after any that were returned in a previous request. Uses the {@link FollowDao} to
     * get the followees.
     *
     * @param request contains the data required to fulfill the request.
     * @return the followees.
     */
    public GetFollowingResponse getFollowees(GetFollowingRequest request) {
        if (request.getFollowerAlias() == null) {
            throw new RuntimeException("[BadRequest] Request missing a follower alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request missing a positive limit");
        }
        return getFollowDaoTempFixme().getFollowees(request);
    }

    /**
     * Returns an instance of {@link FollowDao}. Allows mocking of the FollowDao class
     * for testing purposes. All usages of FollowDao should get their FollowDao
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */


    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
        if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[BadRequest] Request missing a followee alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request missing a positive limit");
        }
        return getFollowDaoTempFixme().getFollowers(request);
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getFollower() == null) {
            throw new RuntimeException("[BadRequest] Request missing a follower");
        } else if (request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request missing a followee");
        }

        return getFollowDaoTempFixme().isFollower(request);
    }

    public Response follow(FollowRequest request) {
        if (request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request missing a followee");
        }

        return getFollowDaoTempFixme().follow(request);
    }

    public Response unfollow(UnfollowRequest request) {
        if (request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request missing a followee");
        }

        return getFollowDaoTempFixme().unfollow(request);
    }
}
