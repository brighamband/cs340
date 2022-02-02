package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

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

    public AuthenticateTask(String username, String password, Handler messageHandler) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }
}
