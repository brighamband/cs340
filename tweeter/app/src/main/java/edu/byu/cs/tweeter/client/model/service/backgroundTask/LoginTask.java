package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

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
