package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that creates a new user account and logs in the new user (i.e., starts a session).
 */
public class RegisterTask extends AuthenticateTask {
//    private static final String LOG_TAG = "RegisterTask";

    /**
     * The user's first name.
     */
    private String firstName;
    /**
     * The user's last name.
     */
    private String lastName;
    /**
     * The base-64 encoded bytes of the user's profile image.
     */
    private String image;

    public RegisterTask(String firstName, String lastName, String username, String password,
                        String image, Handler messageHandler) {
        super(username, password, messageHandler);
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }


    @Override
    protected void runTask() {
        //  TODO Milestone 3
    }

    private User getRegisteredUser() {
        return getFakeData().getFirstUser();
    }

    private AuthToken getRegisteredUserAuthToken() {
        return getFakeData().getAuthToken();
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, getRegisteredUser());
        msgBundle.putSerializable(AUTH_TOKEN_KEY, getRegisteredUserAuthToken());
    }
}
