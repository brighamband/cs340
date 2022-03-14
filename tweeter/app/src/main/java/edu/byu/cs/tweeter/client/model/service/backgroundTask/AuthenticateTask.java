package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class AuthenticateTask extends BackgroundTask {
    public static final String AUTH_TOKEN_KEY = "auth-token";
    public static final String USER_KEY = "user";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;

    // Response variables to save

    private User user;
    private AuthToken authToken;

    protected User getUser() {
        return user;
    }

    protected void setUser(User user) {
        this.user = user;
    }

    protected AuthToken getAuthToken() {
        return authToken;
    }

    protected void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    protected String getUsername() {
        return username;
    }

    protected String getPassword() {
        return password;
    }

    public AuthenticateTask(String username, String password, Handler messageHandler) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }
}
