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
import edu.byu.cs.tweeter.server.dao.FollowDao;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowService {

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
        return getFollowingDao().getFollowees(request);
    }

    /**
     * Returns an instance of {@link FollowDao}. Allows mocking of the FollowDao class
     * for testing purposes. All usages of FollowDao should get their FollowDao
     * instance from this method to allow for mocking of the instance.
     *
     * @return the instance.
     */
    FollowDao getFollowingDao() {
        return new FollowDao();
    }

    public GetFollowersResponse getFollowers(GetFollowersRequest request) {
        if (request.getFolloweeAlias() == null) {
            throw new RuntimeException("[BadRequest] Request missing a followee alias");
        } else if (request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request missing a positive limit");
        }
        return getFollowersDao().getFollowers(request);
    }

    FollowDao getFollowersDao() {
        return new FollowDao();
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Request missing a user");
        }
        return getFollowingCountDao().getFolloweeCount(request);
    }

    FollowDao getFollowingCountDao() {
        return new FollowDao();
    }

    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request) {
        if (request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Request missing a user");
        }
        return getFollowersCountDao().getFollowersCount(request);
    }

    FollowDao getFollowersCountDao() {
        return new FollowDao();
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {
        if (request.getFollower() == null) {
            throw new RuntimeException("[BadRequest] Request missing a follower");
        } else if (request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request missing a followee");
        }

        return isFollowerDao().isFollower(request);
    }

    FollowDao isFollowerDao() {
        return new FollowDao();
    }

    public Response follow(FollowRequest request) {
        if (request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request missing a followee");
        }

        return followDao().follow(request);
    }

    FollowDao followDao() {
        return new FollowDao();
    }

    public Response unfollow(UnfollowRequest request) {
        if (request.getFollowee() == null) {
            throw new RuntimeException("[BadRequest] Request missing a followee");
        }

        return unfollowDao().unfollow(request);
    }

    FollowDao unfollowDao() {
        return new FollowDao();
    }

}
