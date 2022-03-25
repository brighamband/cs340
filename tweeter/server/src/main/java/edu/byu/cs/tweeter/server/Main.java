package edu.byu.cs.tweeter.server;

import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.server.lambda.GetFeedHandler;
import edu.byu.cs.tweeter.server.lambda.LoginHandler;

public class Main {
    public static void main(String[] args) {

        LoginRequest loginRequest = new LoginRequest("@brighamband", "brighamband");
        LoginResponse loginResponse = new LoginHandler().handleRequest(loginRequest, null);


        GetFeedRequest getFeedRequest = new GetFeedRequest(loginResponse.getAuthToken(), loginResponse.getUser().getAlias(), 10, null);
        GetFeedHandler getFeedHandler = new GetFeedHandler();
        GetFeedResponse getFeedResponse = getFeedHandler.handleRequest(getFeedRequest, null);

        System.out.println("done");
    }
}
