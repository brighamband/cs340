package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class PostUpdateFeedMessages implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        IDaoFactory factory = new DynamoDaoFactory();
        StatusService service = new StatusService(factory);
        service.postUpdateFeedMessages(event);
        return null;    // No return needed, the UpdateFeed queue will provide the data to the next lambda
    }
}
