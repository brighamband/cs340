package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {

    static final String URL_PATH = "/get-user";

    private ServerFacade serverFacade;

    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private String alias;

    /**
     * The user who was retrieved from the task (whose alias matches the one searched for).
     */
    private User user;


    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(messageHandler, authToken);
        this.alias = alias;
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        GetUserRequest request = new GetUserRequest(getAuthToken(), alias);
        GetUserResponse response = getServerFacade().getUser(request, URL_PATH);

        user = response.getUser();
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }

    public ServerFacade getServerFacade() {
        if (serverFacade == null) {
            serverFacade = new ServerFacade();
        }
        return new ServerFacade();
    }
}
