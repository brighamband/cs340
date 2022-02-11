package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends SimplePresenter {
    @Override
    public String getMsgPrefix() {
        return "Failed to get user's profile: ";
    }

    public interface View<T> extends SimplePresenter.View {
        void displayLoading(boolean displayOn);
        void addItems(List<T> items);
        void changeScreen(User user);
    }

    protected View view;
    protected UserService userService;

    public PagedPresenter(View view) {
        super(view);
        this.view = view;
        userService = new UserService();
    }

    protected static final int PAGE_SIZE = 10;

//    protected User targetUser;
//    protected AuthToken authToken;
    protected T lastItem;
    protected boolean hasMorePages;
    protected boolean isLoading;
//    protected boolean isGettingUser;

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

    public void loadMoreItems(User user) {
        if (!getIsLoading()) {   // This guard is important for avoiding a race condition in the scrolling code.
            setLoading(true);
            view.displayLoading(true);

            getItems(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem);
//            statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new StoryPresenter.GetStoryObserver());
        }
    }

    /**
     * When a User's status or a mention of a User is clicked (open their profile)
     * @param alias
     */
    public void getUserProfile(String alias) {
        view.displayToastMessage("Getting user's profile...");
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserObserver());
    }

    public class GetUserObserver implements UserObserver {
        @Override
        public void handleSuccess(User user) {
            view.changeScreen(user);
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

    public abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);
}
