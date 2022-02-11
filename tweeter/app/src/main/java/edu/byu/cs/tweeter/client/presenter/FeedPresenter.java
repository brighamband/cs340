package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends StatusPresenter {
    public FeedPresenter(View view) {
        super(view);
    }

    @Override
    public void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
        statusService.getFeed(authToken, targetUser, pageSize, lastItem, new GetFeedObserver());
    }

    public class GetFeedObserver extends PagedListObserver {
        @Override
        public String getMsgPrefix() {
            return "Failed to get feed: ";
        }
    }
}
