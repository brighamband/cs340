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

    private static final String LOG_TAG = "LoginTask";

    public static final String SUCCESS_KEY = "success";
    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";
    public static final String MESSAGE_KEY = "message";
    public static final String EXCEPTION_KEY = "exception";

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

    private AuthToken getAuthToken() {
        return getFakeData().getAuthToken();
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, getLoggedInUser());
        msgBundle.putSerializable(AUTH_TOKEN_KEY, getAuthToken());
    }
}
