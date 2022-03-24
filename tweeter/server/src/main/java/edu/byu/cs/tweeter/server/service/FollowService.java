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
public class FollowService extends Service {

  public FollowService(IDaoFactory daoFactory) {
    super(daoFactory);
  }

  /**
   * Empty constructor just for mock tests.
   */
  public FollowService() {}

  /**
   * Returns the users that the user specified in the request is following. Uses
   * information in
   * the request object to limit the number of followees returned and to return
   * the next set of
   * followees after any that were returned in a previous request. Uses the
   * {@link FollowDao} to
   * get the followees.
   *
   * @param request contains the data required to fulfill the request.
   * @return the followees.
   */
  public GetFollowingResponse getFollowees(GetFollowingRequest request) {
    // Validate request
    if (request.getFollowerAlias() == null) {
      throw new RuntimeException("[BadRequest] Request missing a follower alias");
    } else if (request.getLimit() <= 0) {
      throw new RuntimeException("[BadRequest] Request missing a positive limit");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      throw new RuntimeException("[BadRequest] Auth token has expired");
    }

    // Handle failure
    // FIXME

    // Return response
    return null;
//    return daoFactory.getFollowDao().getFollowees(request);
  }

  public GetFollowersResponse getFollowers(GetFollowersRequest request) {
    // Validate request
    if (request.getFolloweeAlias() == null) {
      throw new RuntimeException("[BadRequest] Request missing a followee alias");
    } else if (request.getLimit() <= 0) {
      throw new RuntimeException("[BadRequest] Request missing a positive limit");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      throw new RuntimeException("[BadRequest] Auth token has expired");
    }

    // Handle failure
    // FIXME

    // Return response
    return null;
//    return factory.getFollowDao().getFollowers(request);
  }

  public IsFollowerResponse isFollower(IsFollowerRequest request) {
    // Validate request
    if (request.getFollower() == null) {
      throw new RuntimeException("[BadRequest] Request missing a follower");
    } else if (request.getFollowee() == null) {
      throw new RuntimeException("[BadRequest] Request missing a followee");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      throw new RuntimeException("[BadRequest] Auth token has expired");
    }

    // Handle failure
    // FIXME

    // Return response
    return null;
//    return factory.getFollowDao().isFollower(request);
  }

  public Response follow(FollowRequest request) {
    // Validate request
    if (request.getFollowee() == null) {
      throw new RuntimeException("[BadRequest] Request missing a followee");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      throw new RuntimeException("[BadRequest] Auth token has expired");
    }

    // Handle failure
    // FIXME

    // Return response
    return null;
//    return factory.getFollowDao().follow(request);
  }

  public Response unfollow(UnfollowRequest request) {
    // Validate request
    if (request.getFollowee() == null) {
      throw new RuntimeException("[BadRequest] Request missing a followee");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      throw new RuntimeException("[BadRequest] Auth token has expired");
    }

    // Handle failure
    // FIXME

    // Return response
    return null;
//    return factory.getFollowDao().unfollow(request);
  }
}
