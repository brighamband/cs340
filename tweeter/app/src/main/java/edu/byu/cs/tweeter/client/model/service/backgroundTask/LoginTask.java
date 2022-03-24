package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {

    static final String URL_PATH = "/login";

    private ServerFacade serverFacade;

    public LoginTask(String username, String password, Handler messageHandler) {
        super(username, password, messageHandler);
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        LoginRequest request = new LoginRequest(getUsername(), getPassword());
        LoginResponse response = getServerFacade().login(request, URL_PATH);

        // Failure
        if (!response.isSuccess()) {
            sendFailedMessage(response.getMessage());
            return;
        }

        setUser(response.getUser());
        setAuthToken(response.getAuthToken());
        sendSuccessMessage();
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, getUser());
        msgBundle.putSerializable(AUTH_TOKEN_KEY, getAuthToken());
    }

    public ServerFacade getServerFacade() {
        if (serverFacade == null) {
            serverFacade = new ServerFacade();
        }
        return new ServerFacade();
    }
}
