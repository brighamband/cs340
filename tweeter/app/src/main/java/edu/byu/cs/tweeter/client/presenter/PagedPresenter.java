package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends SimplePresenter {
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

  protected T lastItem;
  protected boolean hasMorePages;
  protected boolean isLoading;

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
    if (!getIsLoading()) { // This guard is important for avoiding a race condition in the scrolling code.
      setLoading(true);
      view.displayLoading(true);

      getItems(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastItem);
    }
  }

  /**
   * When a User's status or a mention of a User is clicked (open their profile)
   * 
   * @param alias
   */
  public void getUserProfile(String alias) {
    view.displayToastMessage("Getting user's profile...");
    userService.getUser(Cache.getInstance().getCurrUserAuthToken(), alias, new GetUserObserver());
  }

  public class GetUserObserver extends Observer implements UserObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to get user's profile";
    }

    @Override
    public void handleSuccess(User user) {
      view.changeScreen(user);
    }
  }

  public abstract void getItems(AuthToken authToken, User targetUser, int pageSize, T lastItem);

  public abstract class PagedListObserver extends Observer implements PagedObserver<T> {
    @Override
    public void handleSuccess(List<T> items, boolean hasMorePages) {
      setLoading(false);
      view.displayLoading(false);

      lastItem = (items.size() > 0) ? items.get(items.size() - 1) : null;
      setHasMorePages(hasMorePages);
      view.addItems(items);
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
      view.displayToastMessage(getMsgPrefix() + " because of exception: " + exception.getMessage());
    }
  }
}
