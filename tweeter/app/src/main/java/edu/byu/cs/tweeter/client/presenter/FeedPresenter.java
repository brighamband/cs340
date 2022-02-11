package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
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

    public class GetFeedObserver extends Observer implements PagedObserver<Status> {
        @Override
        public String getMsgPrefix() {
            return "Failed to get feed: ";
        }

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
}
