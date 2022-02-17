package edu.byu.cs.tweeter.client.presenter;

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

  public class GetFollowingObserver extends PagedListObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to get following";
    }
  }
}
