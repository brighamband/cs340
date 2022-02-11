package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus,
                          PagedObserver<Status> getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetStoryHandler(getStoryObserver));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus,
                        PagedObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(getFeedObserver));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void postStatus(AuthToken currUserAuthToken, Status newStatus, SimpleObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                newStatus, new PostStatusHandler(postStatusObserver));
        BackgroundTaskUtils.runTask(statusTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends BackgroundTaskHandler<PagedObserver<Status>> {

        public GetStoryHandler(PagedObserver<Status> getStoryObserver) {
            super(getStoryObserver);
        }

        @Override
        protected void handleSuccessMessage(PagedObserver<Status> observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(GetStoryTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetStoryTask.MORE_PAGES_KEY);
            observer.handleSuccess(statuses, hasMorePages);
        }
    }



    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends BackgroundTaskHandler<PagedObserver<Status>> {

        public GetFeedHandler(PagedObserver<Status> getFeedObserver) {
            super(getFeedObserver);
        }

        @Override
        protected void handleSuccessMessage(PagedObserver<Status> observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
            observer.handleSuccess(statuses, hasMorePages);
        }
    }

    private class PostStatusHandler extends Handler {
        private SimpleObserver postStatusObserver;

        public PostStatusHandler(SimpleObserver postStatusObserver) {
            this.postStatusObserver = postStatusObserver;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
            if (success) {
                postStatusObserver.handleSuccess();
            } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
                postStatusObserver.handleFailure(message);
            } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);
                postStatusObserver.handleException(ex);
            }
        }
    }
}
