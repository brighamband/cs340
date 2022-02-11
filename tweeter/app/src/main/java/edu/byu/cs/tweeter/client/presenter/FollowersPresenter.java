package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends FollowPresenter {

    @Override
    public String getMsgPrefix() {
        return null;
    }

//    public interface View {
//        void displayToastMessage(String message);
//        void displayLoading(boolean displayOn);
//        void addFollowers(List<User> followers);
//        void displayUserFollower(User user);
//    }

//    private View view;
//    private FollowService followService;
//    private UserService userService;

    public FollowersPresenter(View view) {
        super(view);
//        this.view = view;
//        followService = new FollowService();
//        userService = new UserService();
    }

//    private User lastFollower;
//    private boolean hasMorePages;
//    private boolean isLoading = false;
//
//    public boolean getHasMorePages() {
//        return hasMorePages;
//    }
//
//    public void setHasMorePages(boolean hasMorePages) {
//        this.hasMorePages = hasMorePages;
//    }
//
//    public boolean getIsLoading() {
//        return isLoading;
//    }
//
//    public void setLoading(boolean loading) {
//        isLoading = loading;
//    }

    @Override
    public void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        followService.getFollowers(authToken, targetUser, pageSize, lastItem, new GetFollowersObserver());
    }

//    public void loadMoreFollowers(User user) {
//        if (!getIsLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
//            setLoading(true);
//            view.displayLoading(true);
//
//            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new FollowersPresenter.GetFollowersObserver());
//        }
//    }

    public class GetFollowersObserver implements PagedObserver<User> {
        @Override
        public void handleSuccess(List<User> followers, boolean hasMorePages) {
            setLoading(false);
            view.displayLoading(false);

            lastItem = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(followers);
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

//    /**
//     * USER -- TODO in M3 -- DUPLICATED
//     */
//
//    /**
//     * When a User's status or a mention of a User is clicked (open their profile)
//     * @param alias
//     */
//    public void onUserProfileClick(String alias) {
//        view.displayToastMessage("Getting user's profile...");
//        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserObserver());
//    }
//
//    public class GetUserObserver implements UserObserver {
//        @Override
//        public void handleSuccess(User user) {
//            view.displayUserFollower(user);
//        }
//
//        @Override
//        public void handleFailure(String message) {
//            view.displayToastMessage("Failed to get user's profile: " + message);
//        }
//
//        @Override
//        public void handleException(Exception exception) {
//            view.displayToastMessage("Failed to get user's profile because of exception: " + exception.getMessage());
//        }
//    }
}
