package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class GetFeedHandler implements RequestHandler<GetFeedRequest, GetFeedResponse> {

    @Override
    public GetFeedResponse handleRequest(GetFeedRequest request, Context context) {
        IDaoFactory factory = new DynamoDaoFactory();
        StatusService service = new StatusService(factory);
        return service.getFeed(request);
    }
}
