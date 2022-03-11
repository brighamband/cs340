package edu.byu.cs.tweeter.server.service;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService {
    public Response postStatus(PostStatusRequest request) {
        if(request.getStatus() == null) {
            throw new RuntimeException("[BadRequest] Request missing a status");
        }

        // TODO: uses the dummy data.  Replace with a real implementation.
        return new Response(true);
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if(request.getTargetUserAlias() == null) {
            throw new RuntimeException("[BadRequest] Request missing a target user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request missing a positive limit");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        Pair<List<Status>, Boolean> dummyFeedPages = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new GetFeedResponse(dummyFeedPages.getFirst(), dummyFeedPages.getSecond());
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
        if(request.getTargetUserAlias() == null) {
            throw new RuntimeException("[BadRequest] Request missing a target user alias");
        } else if(request.getLimit() <= 0) {
            throw new RuntimeException("[BadRequest] Request missing a positive limit");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        Pair<List<Status>, Boolean> dummyFeedPages = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
        return new GetStoryResponse(dummyFeedPages.getFirst(), dummyFeedPages.getSecond());
    }

    FakeData getFakeData() {
        return new FakeData();
    }
}
