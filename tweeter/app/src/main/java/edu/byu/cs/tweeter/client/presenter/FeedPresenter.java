package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends StatusPresenter {
    @Override
    public String getMsgPrefix() {
        return "Failed to get feed: ";
    }
//    private static final int PAGE_SIZE = 10;

//    public interface View extends PagedPresenter.View<Status> {
////        void displayToastMessage(String message);
////        void displayLoading(boolean displayOn);
////        void addStatuses(List<Status> statuses);
////        void changeScreen(User user);
//        void openLinkInBrowser(String urlLink);
//    }

//    private View view;
//    private StatusService statusService;
//    private UserService userService;

    public FeedPresenter(View view) {
        super(view);
//        this.view = view;
//        statusService = new StatusService();
//        userService = new UserService();
    }

//    private Status lastStatus;
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
    public void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
        statusService.getFeed(authToken, targetUser, pageSize, lastItem, new GetFeedObserver());
    }

//    public void loadMoreFeed(User user) {
//        if (!getIsLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
//            setLoading(true);
//            view.displayLoading(true);
//
//            statusService.getFeed(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new GetFeedObserver());
//        }
//    }

    public class GetFeedObserver implements PagedObserver<Status> {
        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages) {
            setLoading(false);
            view.displayLoading(false);

            lastItem = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.addItems(statuses);
        }

        @Override
        public void handleFailure(String message) {
            setLoading(false);
            view.displayLoading(false);
            view.displayToastMessage(getMsgPrefix() + message);
        }

        @Override
        public void handleException(Exception exception) {
            setLoading(false);
            view.displayLoading(false);
            view.displayToastMessage(getMsgPrefix() + "because of exception: " + exception.getMessage());
        }
    }

    /**
     * User
     */

//    public void onUserMentionClick(String urlOrAliasLink) {
//        if (urlOrAliasLink.contains("http")) {
//            view.openLinkInBrowser(urlOrAliasLink);
//        } else {
//            onUserProfileClick(urlOrAliasLink);
//        }
//    }

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
//            view.changeScreen(user);
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
