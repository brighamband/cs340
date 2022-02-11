package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends FollowPresenter {
    public FollowersPresenter(View view) {
        super(view);
    }

    @Override
    public void getItems(AuthToken authToken, User targetUser, int pageSize, User lastItem) {
        followService.getFollowers(authToken, targetUser, pageSize, lastItem, new GetFollowersObserver());
    }

    public class GetFollowersObserver extends PagedListObserver {
        @Override
        public String getMsgPrefix() {
            return "Failed to get followers: ";
        }
    }
}
