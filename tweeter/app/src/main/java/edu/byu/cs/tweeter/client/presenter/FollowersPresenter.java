package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {
    private static final int PAGE_SIZE = 10;

    public interface View {
        void displayToastMessage(String message);
        void displayLoading(boolean displayOn);
        void addFollowers(List<User> followers);
        void displayUserFollower(User user);
    }

    private View view;
    private FollowService followService;
    private UserService userService;

    public FollowersPresenter(View view) {
        this.view = view;
        followService = new FollowService();
        userService = new UserService();
    }

    private User lastFollower;
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

    public void loadMoreFollowers(User user) {
        if (!getIsLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.displayLoading(true);

            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new FollowersPresenter.GetFollowersObserver());
        }
    }

    public class GetFollowersObserver implements FollowService.GetFollowersObserver {
        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            setLoading(false);
            view.displayLoading(false);

            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFollowers(followers);
        }

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            view.displayLoading(false);
            view.displayToastMessage("Failed to get followers: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            setLoading(false);
            view.displayLoading(false);
            view.displayToastMessage("Failed to get followers because of exception: " + exception.getMessage());
        }
    }

    /**
     * USER -- TODO in M3 -- DUPLICATED
     */

    public void getUser(String alias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserObserver());

    }

    public class GetUserObserver implements UserService.GetUserObserver {
        @Override
        public void handleSuccess(User user) {
            // FIXME - Anything here with loading?
            view.displayUserFollower(user);
        }

        @Override
        public void handleFailure(String message) {
            // FIXME - Anything here with loading?
            view.displayToastMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            // FIXME - Anything here with loading?
            view.displayToastMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
