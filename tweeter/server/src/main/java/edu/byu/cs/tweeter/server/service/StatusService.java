package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.server.TimeUtils;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends Service {

  private final int MAX_NUM_FOLLOWERS = 1000000;

  public StatusService(IDaoFactory daoFactory) {
    super(daoFactory);
  }

  public Response postStatus(PostStatusRequest request) {
    // Validate request
    if (request.getStatus() == null) {
      throw new RuntimeException("[BadRequest] Request missing a status");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      return new Response(false, "Auth token has expired. Log back in again to keep using Tweeter.");
    }

    // New status info
    String authorAlias = request.getStatus().getUser().getAlias();
    long timestamp = TimeUtils.getCurrTimeInMs();
    String post = request.getStatus().getPost();

    // Have StoryDao create a new status in Story table (postStatus)
    boolean successful = daoFactory.getStoryDao().create(authorAlias, timestamp, post);
    if (!successful) {  // Handle failure case #1
      throw new RuntimeException("[ServerError] Unable to add status to Story table");
    }

    // Have FollowDao get user's followers
    List<String> followerAliases = daoFactory.getFollowDao().getFollowers(
            new GetFollowersRequest(request.getAuthToken(), authorAlias, MAX_NUM_FOLLOWERS, null)
    ).getFirst();
    // Have FeedDao create that same new status in each feed of user's followers
    for (String viewerAlias : followerAliases) {
      successful = daoFactory.getFeedDao().create(viewerAlias, timestamp, post, authorAlias);
      if (!successful) {  // Handle failure case #2
        throw new RuntimeException("[ServerError] Unable to add status to Feed table");
      }
    }

    // Return Response
    return new Response(true);
  }

  public GetStoryResponse getStory(GetStoryRequest request) {
    // Validate request
    if (request.getTargetUserAlias() == null) {
      throw new RuntimeException("[BadRequest] Request missing a target user alias");
    } else if (request.getLimit() <= 0) {
      throw new RuntimeException("[BadRequest] Request missing a positive limit");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      return new GetStoryResponse("Auth token has expired. Log back in again to keep using Tweeter.");
    }



    // Handle failure
    // FIXME

    // Return response
    // TODO: Generates dummy data. Replace with a real implementation.
    Pair<List<Status>, Boolean> dummyStoryPages = getFakeData().getPageOfStatus(request.getLastStatus(),
            request.getLimit());
    return new GetStoryResponse(dummyStoryPages.getFirst(), dummyStoryPages.getSecond());
  }

  public GetFeedResponse getFeed(GetFeedRequest request) {
    // Validate request
    if (request.getTargetUserAlias() == null) {
      throw new RuntimeException("[BadRequest] Request missing a target user alias");
    } else if (request.getLimit() <= 0) {
      throw new RuntimeException("[BadRequest] Request missing a positive limit");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      return new GetFeedResponse("Auth token has expired. Log back in again to keep using Tweeter.");
    }

    // Handle failure
    // FIXME

    // Return response
    // TODO: Generates dummy data. Replace with a real implementation.
    Pair<List<Status>, Boolean> dummyFeedPages = getFakeData().getPageOfStatus(request.getLastStatus(),
        request.getLimit());
    return new GetFeedResponse(dummyFeedPages.getFirst(), dummyFeedPages.getSecond());
  }

  FakeData getFakeData() {
    return new FakeData();
  }
}
