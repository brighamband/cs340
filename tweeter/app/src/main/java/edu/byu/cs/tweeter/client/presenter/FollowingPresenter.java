package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {
    private static final int PAGE_SIZE = 10;

    public interface View {
        void displayErrorMessage(String message);
        void displayLoading(boolean displayOn);
        void addFollowing(List<User> following);
        void displayUserFollowing(User user);
    }

    private View view;
    private FollowService followService;
    private UserService userService;

    public FollowingPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    private User lastFollowing;
    private boolean hasMorePages;
    private boolean isLoading = false;

    public boolean getHasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean getIsLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void loadMoreFollowing(User user) {
        if (!getIsLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.displayLoading(true);

            followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollowing, new GetFollowingObserver());
        }
    }

    public class GetFollowingObserver implements FollowService.GetFollowingObserver {
        @Override
        public void handleSuccess(List<User> following, boolean hasMorePages) {
            setLoading(false);
            view.displayLoading(false);

            lastFollowing = (following.size() > 0) ? following.get(following.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFollowing(following);
        }

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            view.displayLoading(false);
            view.displayErrorMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            setLoading(false);
            view.displayLoading(false);
            view.displayErrorMessage("Failed to get following because of exception: " + exception.getMessage());
        }
    }

    /**
     * USER    // FIXME - DUPLICATED
     */

    public void getUser(String alias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserObserver());
    }

    public class GetUserObserver implements UserService.GetUserObserver {
        @Override
        public void handleSuccess(User user) {
            // FIXME - Anything here with loading?
            view.displayUserFollowing(user);
        }

        @Override
        public void handleFailure(String message) {
            // FIXME - Anything here with loading?
            view.displayErrorMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            // FIXME - Anything here with loading?
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
