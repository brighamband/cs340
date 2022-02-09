package edu.byu.cs.tweeter.client.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class RegisterPresenter {
    public interface View {
        void displayToastMessage(String message);
        void bypassRegisterScreen(User registeredUser, String registeredAlias);
        void setErrorViewText(String text);
    }

    private View view;
    private UserService userService;

    public RegisterPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void onRegisterButtonClick(String firstName, String lastName, String alias,
                                      String password, ImageView imageToUpload) {
        try {
            validateRegistration(firstName, lastName, alias, password, imageToUpload);
            view.setErrorViewText(null);
            view.displayToastMessage("Registering...");

            Bitmap image = ((BitmapDrawable) imageToUpload.getDrawable()).getBitmap();
            userService.register(firstName, lastName, alias, password, image, new RegisterObserver());
        } catch (Exception e) {
            view.setErrorViewText(e.getMessage());
        }
    }

    public void validateRegistration(String firstName, String lastName, String alias,
                                     String password, ImageView imageToUpload) {
        if (firstName.length() == 0) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName.length() == 0) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }
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

        if (imageToUpload.getDrawable() == null) {
            throw new IllegalArgumentException("Profile image must be uploaded.");
        }
    }

    public class RegisterObserver implements UserObserver {
        @Override
        public void handleSuccess(User registeredUser) {
            String registeredAlias = Cache.getInstance().getCurrUser().getName();
            view.bypassRegisterScreen(registeredUser, registeredAlias);
        }

        @Override
        public void handleFailure(String message) {
            view.displayToastMessage("Failed to register: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayToastMessage("Failed to register because of exception: " + exception);
        }
    }
}
