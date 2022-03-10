package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

public class StatusService {
    public Response postStatus(PostStatusRequest request) {
        if(request.getStatus() == null) {
            throw new RuntimeException("[BadRequest] Request missing a status");
        }

        return new Response(true);
    }
}
