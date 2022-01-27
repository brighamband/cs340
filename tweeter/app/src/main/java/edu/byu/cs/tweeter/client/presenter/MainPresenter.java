package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class MainPresenter {
    public interface View {
        void returnToLoginScreen();
        void displayErrorMessage(String message);
    }

    private View view;
    private UserService userService;

    public MainPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void logOut() {
        userService.logOut(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
    }

    public class LogoutObserver implements UserService.LogoutObserver {
        @Override
        public void handleSuccess() {
            view.returnToLoginScreen();
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to logout: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to logout because of exception: " + exception.getMessage());
        }
    }
}
