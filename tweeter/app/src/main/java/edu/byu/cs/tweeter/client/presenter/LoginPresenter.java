package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter extends SimplePresenter {
    public interface View extends SimplePresenter.View {
        void bypassLoginScreen(User loggedInUser, String loggedInAlias);
        void setErrorViewText(String text);
    }

    private View view;
    private UserService userService;

    public LoginPresenter(View view) {
        super(view);
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

    public class LoginObserver extends Observer implements UserObserver {
        @Override
        public String getMsgPrefix() {
            return "Failed to login: ";
        }

        @Override
        public void handleSuccess(User loggedInUser) {
            String loggedInAlias = Cache.getInstance().getCurrUser().getName();
            view.bypassLoginScreen(loggedInUser, loggedInAlias);
        }

        @Override
        public void handleFailure(String message) {
            view.displayToastMessage(getMsgPrefix() + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayToastMessage(getMsgPrefix() + "because of exception: " + exception.getMessage());
        }
    }
}
