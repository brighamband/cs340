package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {
    private static final int PAGE_SIZE = 10;

    public interface View {
        void displayToastMessage(String message);
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

    private User lastFollowee;
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

    public void loadMoreFollowees(User user) {
        if (!getIsLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.displayLoading(true);

            followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());
        }
    }

    public class GetFollowingObserver implements PagedObserver<User> {
        @Override
        public void handleSuccess(List<User> followees, boolean hasMorePages) {
            setLoading(false);
            view.displayLoading(false);

            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addFollowing(followees);
        }

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            view.displayLoading(false);
            view.displayToastMessage("Failed to get following: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            setLoading(false);
            view.displayLoading(false);
            view.displayToastMessage("Failed to get following because of exception: " + exception.getMessage());
        }
    }

    /**
     * USER    // FIXME - DUPLICATED
     */

    /**
     * When a User's status or a mention of a User is clicked (open their profile)
     * @param alias
     */
    public void onUserProfileClick(String alias) {
        view.displayToastMessage("Getting user's profile...");
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserObserver());
    }

    public class GetUserObserver implements UserObserver {
        @Override
        public void handleSuccess(User user) {
            view.displayUserFollowing(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayToastMessage("Failed to get user's profile: " + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayToastMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
