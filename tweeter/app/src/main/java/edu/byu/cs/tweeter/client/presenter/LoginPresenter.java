package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {
    public interface View {
        void displayToastMessage(String message);
        void bypassLoginScreen(User loggedInUser, String loggedInAlias);
        void setErrorViewText(String text);
    }

    private View view;
    private UserService userService;

    public LoginPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void onLoginButtonClick(String alias, String password) {
        try {
            validateLogin(alias, password);
            view.setErrorViewText(null);
            view.displayToastMessage("Logging In...");

            userService.logIn(alias, password, new LoginObserver());
        } catch (Exception e) {
            view.setErrorViewText(e.getMessage());
        }
    }

    public void logIn(String alias, String password) {
        userService.logIn(alias, password, new LoginObserver());
    }

    public void validateLogin(String alias, String password) {
        if (alias.length() == 0) {
            throw new IllegalArgumentException("Alias cannot be empty.");
        }
        if (alias.charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public class LoginObserver implements UserService.LoginObserver {
        @Override
        public void handleSuccess(User loggedInUser) {
            String loggedInAlias = Cache.getInstance().getCurrUser().getName();
            view.bypassLoginScreen(loggedInUser, loggedInAlias);
        }

        @Override
        public void handleFailure(String message) {
            view.displayToastMessage("Failed to login: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayToastMessage("Failed to login because of exception: " + exception.getMessage());
        }
    }
}
