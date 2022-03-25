package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends Service {

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

    // Have StoryDao to post a new status
    // FIXME

    // Handle failure
    // FIXME

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
