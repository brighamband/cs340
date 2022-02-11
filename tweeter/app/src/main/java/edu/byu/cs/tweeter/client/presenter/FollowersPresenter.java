package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
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

    public class GetFollowersObserver extends Observer implements PagedObserver<User> {
        @Override
        public String getMsgPrefix() {
            return "Failed to get followers: ";
        }

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
