package edu.byu.cs.tweeter.server.service;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.dynamo.FollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.util.Pair;

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
  public FollowService() {
  }

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
      return new GetFollowingResponse("Auth token has expired. Log back in again to keep using Tweeter.");
    }

    // Have FollowDao get followees data
    Pair<List<String>, Boolean> result = daoFactory.getFollowDao().getFollowees(request);
    List<String> followeeAliases = result.getFirst();
    boolean hasMorePages = result.getSecond();

    // Make list of followees to return
    List<User> followees = new ArrayList<>();
    for (String alias : followeeAliases) {
      User followee = daoFactory.getUserDao().getUser(alias);
      if (followee == null) {
        throw new RuntimeException("[ServerError] Couldn't find user after their alias was listed as followee");
      }
      followees.add(followee);
    }

    // Handle failure
    if (followees == null && hasMorePages) {
      throw new RuntimeException("[ServerException] GetFollowees calculation not working properly");
    }

    // Return response
    return new GetFollowingResponse(followees, hasMorePages);
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
      return new GetFollowersResponse("Auth token has expired. Log back in again to keep using Tweeter.");
    }

    // Have FollowDao get followers data
    Pair<List<String>, Boolean> result = daoFactory.getFollowDao().getFollowers(request);
    List<String> followerAliases = result.getFirst();
    Boolean hasMorePages = result.getSecond();

    // Make list of followers to return
    List<User> followers = new ArrayList<>();
    for (String alias : followerAliases) {
      User follower = daoFactory.getUserDao().getUser(alias);
      if (follower == null) {
        throw new RuntimeException("[ServerError] Couldn't find user after their alias was listed as follower");
      }
      followers.add(follower);
    }

    // Handle failure
    if (followers == null && hasMorePages == null) {
      throw new RuntimeException("[ServerException] GetFollowers calculation not working properly");
    }

    // Return response
    return new GetFollowersResponse(followers, hasMorePages);
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
      return new IsFollowerResponse("Auth token has expired. Log back in again to keep using Tweeter.");
    }

    // Have FollowDao check if the followee is being followed by the follower
    Boolean isFollower = daoFactory.getFollowDao().isFollower(
        request.getFollower().getAlias(),
        request.getFollowee().getAlias());

    // Handle failure
    if (isFollower == null) {
      throw new RuntimeException("[ServerError] Unable to check isFollower");
    }

    // Return response
    return new IsFollowerResponse(isFollower);
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
      return new Response(false, "Auth token has expired. Log back in again to keep using Tweeter.");
    }

    String followerAlias = daoFactory.getAuthTokenDao().getCurrUserAlias(request.getAuthToken().getToken());
    String followeeAlias = request.getFollowee().getAlias();

    // Make sure they aren't already following them
    Boolean isFollower = daoFactory.getFollowDao().isFollower(followerAlias, followeeAlias);
    if (isFollower == null) {
      throw new RuntimeException("[ServerError] Unable to check isFollower");
    }
    if (isFollower) {
      return new Response(false, followerAlias + " is already following " + followeeAlias);
    }

    // Create follows relationship
    boolean successful = daoFactory.getFollowDao().create(followerAlias, request.getFollowee().getAlias());

    // Handle failure
    if (!successful) {
      throw new RuntimeException("[ServerError] Unable to follow");
    }

    // Increment the follower user's following count
    int followerCurrFollowingCount = daoFactory.getUserDao().getFollowingCount(followerAlias);
    daoFactory.getUserDao().setFollowingCount(followerAlias, followerCurrFollowingCount + 1);

    // Increment the followee user's followers count
    int followeeCurrFollowersCount = daoFactory.getUserDao().getFollowersCount(followeeAlias);
    daoFactory.getUserDao().setFollowersCount(followeeAlias, followeeCurrFollowersCount + 1);

    // Return response
    return new Response(true);
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
      return new Response(false, "Auth token has expired. Log back in again to keep using Tweeter.");
    }

    String followerAlias = daoFactory.getAuthTokenDao().getCurrUserAlias(request.getAuthToken().getToken());
    String followeeAlias = request.getFollowee().getAlias();

    // Make sure they currently are following them
    Boolean isFollower = daoFactory.getFollowDao().isFollower(followerAlias, followeeAlias);
    if (isFollower == null) {
      throw new RuntimeException("[ServerError] Unable to check isFollower");
    }
    if (!isFollower) {
      return new Response(false, followerAlias + " isn't currently following " + followeeAlias);
    }

    // Delete follows relationship (unfollow)
    boolean successful = daoFactory.getFollowDao().remove(followerAlias, followeeAlias);

    // Handle failure
    if (!successful) {
      throw new RuntimeException("[ServerError] Unable to unfollow");
    }

    // Decrement the follower user's following count
    int followerCurrFollowingCount = daoFactory.getUserDao().getFollowingCount(followerAlias);
    daoFactory.getUserDao().setFollowingCount(followerAlias, followerCurrFollowingCount - 1);

    // Decrement the followee user's followers count
    int followeeCurrFollowersCount = daoFactory.getUserDao().getFollowersCount(followeeAlias);
    daoFactory.getUserDao().setFollowersCount(followeeAlias, followeeCurrFollowersCount - 1);

    // Return response
    return new Response(true);
  }
}
