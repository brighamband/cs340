package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class PagedPresenter<T> extends SimplePresenter {
    @Override
    public String getMsgPrefix() {
        return "Failed to get user's profile ";
    }

    public interface View<T> extends SimplePresenter.View {
        void displayLoading(boolean displayOn);
        // TODO add more
//        void navigateToItem(User user);
        void addItemsToList(List<T> items);
    }

    private static final int PAGE_SIZE = 10;

//    protected User targetUser;
//    protected AuthToken authToken;
//    protected T lastItem;
//    protected boolean hasMorePages;
    protected boolean isLoading;
//    protected boolean isGettingUser;

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

            statusService.getStory(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastStatus, new StoryPresenter.GetStoryObserver());
        }
    }
}
