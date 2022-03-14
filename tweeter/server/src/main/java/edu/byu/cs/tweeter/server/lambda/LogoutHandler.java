package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.service.UserService;

public class LogoutHandler implements RequestHandler<LogoutRequest, Response> {

    @Override
    public Response handleRequest(LogoutRequest request, Context context) {
        UserService userService = new UserService();
        return userService.logout();
    }
}
