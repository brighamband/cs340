package edu.byu.cs.tweeter.client.model.service;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {
    /**
     * User
     */

    public interface GetUserObserver {
        void handleSuccess(User user);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void getUser(AuthToken currUserAuthToken, String alias, GetUserObserver getUserObserver) {
        GetUserTask getUserTask = new GetUserTask(currUserAuthToken, alias, new GetUserHandler(getUserObserver));
        BackgroundTaskUtils.runTask(getUserTask);
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandler extends Handler {
        private GetUserObserver observer;

        public GetUserHandler(GetUserObserver observer) {
            this.observer = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                observer.handleSuccess(user);
            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                observer.handleFailure(message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                observer.handleException(ex);
            }
        }
    }

    /**
     * Login
     */

    public interface LoginObserver {
        void handleSuccess(User loggedInUser);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void logIn(String alias, String password, LoginObserver loginObserver) {
        LoginTask loginTask = new LoginTask(alias, password, new LoginHandler(loginObserver));
        BackgroundTaskUtils.runTask(loginTask);
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends Handler {
        private LoginObserver loginObserver;

        public LoginHandler(LoginObserver loginObserver) {
            this.loginObserver = loginObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) {
                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

                // Cache user session information
                Cache.getInstance().setCurrUser(loggedInUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);

                loginObserver.handleSuccess(loggedInUser);
            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                loginObserver.handleFailure(message);
            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                loginObserver.handleException(ex);
            }
        }
    }

    /**
     * Register
     */

    public interface RegisterObserver {
        void handleSuccess(User registeredUser);
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void register(String firstName, String lastName, String alias,
                         String password, Bitmap image, RegisterObserver registerObserver) {
        // Convert image to byte array.
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] imageBytes = bos.toByteArray();

        // Intentionally, Use the java Base64 encoder so it is compatible with M4.
        String imageBytesBase64 = Base64.getEncoder().encodeToString(imageBytes);

        // Execute task
        RegisterTask registerTask = new RegisterTask(firstName, lastName, alias, password,
                imageBytesBase64, new RegisterHandler(registerObserver));
        BackgroundTaskUtils.runTask(registerTask);
    }

    // RegisterHandler

    private class RegisterHandler extends Handler {
        private RegisterObserver registerObserver;

        public RegisterHandler(RegisterObserver registerObserver) {
            this.registerObserver = registerObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
            if (success) {
                User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);

                // Cache user session information
                Cache.getInstance().setCurrUser(registeredUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);

                registerObserver.handleSuccess(registeredUser);
            } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);
                registerObserver.handleFailure(message);
            } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);
                registerObserver.handleException(ex);
            }
        }
    }

    /**
     * Logout
     */

    public interface LogoutObserver {
        void handleSuccess();
        void handleFailure(String message);
        void handleException(Exception exception);
    }

    public void logOut(AuthToken currUserAuthToken, LogoutObserver logoutObserver) {
        LogoutTask logoutTask = new LogoutTask(currUserAuthToken, new LogoutHandler(logoutObserver));
        BackgroundTaskUtils.runTask(logoutTask);
    }

    // LogoutHandler

    private class LogoutHandler extends Handler {
        private LogoutObserver logoutObserver;

        public LogoutHandler(LogoutObserver logoutObserver) {
            this.logoutObserver = logoutObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
            if (success) {
                // Clear user data (cached data)
                Cache.getInstance().clearCache();

                logoutObserver.handleSuccess();
            } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
                logoutObserver.handleFailure(message);
            } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
                logoutObserver.handleException(ex);
            }
        }
    }
}
