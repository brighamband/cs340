package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that logs in a user (i.e., starts a session).
 */
public class LoginTask extends AuthenticateTask {
//    private static final String LOG_TAG = "LoginTask";

    public LoginTask(String username, String password, Handler messageHandler) {
        super(username, password, messageHandler);
    }

    @Override
    protected void runTask() {
        //  TODO Milestone 3
    }

    private User getLoggedInUser() {
        return getFakeData().getFirstUser();
    }

    private AuthToken getLoggedInUserAuthToken() {
        return getFakeData().getAuthToken();
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, getLoggedInUser());
        msgBundle.putSerializable(AUTH_TOKEN_KEY, getLoggedInUserAuthToken());
    }
}
