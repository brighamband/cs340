package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.server.service.FollowService;

public class GetFollowersHandler implements RequestHandler<GetFollowersRequest, GetFollowersResponse> {

    @Override
    public GetFollowersResponse handleRequest(GetFollowersRequest request, Context context) {
        FollowService service = new FollowService();
        return service.getFollowers(request);
    }
}
