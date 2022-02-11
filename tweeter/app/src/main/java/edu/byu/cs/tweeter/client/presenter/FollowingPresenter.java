package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends FollowPresenter {
  public FollowingPresenter(View view) {
    super(view);
  }

  @Override
  public void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
    followService.getFollowing(authToken, targetUser, pageSize, lastItem, new GetFollowingObserver());
  }

  public class GetFollowingObserver extends Observer implements PagedObserver<User> {
    @Override
    public String getMsgPrefix() {
      return "Failed to get following: ";
    }

    @Override
    public void handleSuccess(List<User> followees, boolean hasMorePages) {
      setLoading(false);
      view.displayLoading(false);

      lastItem = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;
      setHasMorePages(hasMorePages);
      view.addItems(followees);
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
}
