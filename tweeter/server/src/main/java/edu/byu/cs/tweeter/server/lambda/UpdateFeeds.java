package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.server.service.StatusService;

public class UpdateFeeds implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        IDaoFactory factory = new DynamoDaoFactory();
        StatusService service = new StatusService(factory);
        service.updateFeeds(event);
        return null;    // No return needed, done with pipeline and msg success was determined after poster's story updated
    }
}
