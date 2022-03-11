package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.response.Response;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {

    static final String URL_PATH = "/follow";

    private ServerFacade serverFacade;

    /**
     * The user that is being followed.
     */
    private User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.followee = followee;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        FollowRequest request = new FollowRequest(getAuthToken(), followee);
        Response response = getServerFacade().follow(request, URL_PATH);
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        //  N/A - Doesn't need to do anything
    }

    public ServerFacade getServerFacade() {
        if (serverFacade == null) {
            serverFacade = new ServerFacade();
        }
        return new ServerFacade();
    }
}
