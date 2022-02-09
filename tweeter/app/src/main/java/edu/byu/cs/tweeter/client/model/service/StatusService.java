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
    /**
     * Story
     */

//    public interface GetStoryObserver extends PagedObserver<Status> {
//        void handleSuccess(List<Status> statuses, boolean hasMorePages);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus,
                          PagedObserver<Status> getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetStoryHandler(getStoryObserver));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    /**
     * Message handler (i.e., observer) for GetStoryTask.
     */
    private class GetStoryHandler extends BackgroundTaskHandler<PagedObserver<Status>> {
//        private PagedObserver<Status> getStoryObserver;

        public GetStoryHandler(PagedObserver<Status> getStoryObserver) {
            super(getStoryObserver);
//            this.getStoryObserver = getStoryObserver;
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
//            if (success) {
//                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);
//                getStoryObserver.handleSuccess(statuses, hasMorePages);
//            } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);
//                getStoryObserver.handleFailure(message);
//            } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);
//                getStoryObserver.handleException(ex);
//            }
//        }

        @Override
        protected void handleSuccessMessage(PagedObserver<Status> observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(GetStoryTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetStoryTask.MORE_PAGES_KEY);
            observer.handleSuccess(statuses, hasMorePages);
        }
    }

    /**
     * Feed
     */

//    public interface PagedObserver<Status extends PagedObserver<Status> {
//        void handleSuccess(List<Status> statuses, boolean hasMorePages);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus,
                        PagedObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new GetFeedHandler(getFeedObserver));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    /**
     * Message handler (i.e., observer) for GetFeedTask.
     */
    private class GetFeedHandler extends BackgroundTaskHandler<PagedObserver<Status>> {
//        private PagedObserver<Status> getFeedObserver;

        public GetFeedHandler(PagedObserver<Status> getFeedObserver) {
            super(getFeedObserver);
//            this.getFeedObserver = getFeedObserver;
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFeedTask.SUCCESS_KEY);
//            if (success) {
//                List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetFeedTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFeedTask.MORE_PAGES_KEY);
//                getFeedObserver.handleSuccess(statuses, hasMorePages);
//            } else if (msg.getData().containsKey(GetFeedTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFeedTask.MESSAGE_KEY);
//                getFeedObserver.handleFailure(message);
//            } else if (msg.getData().containsKey(GetFeedTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFeedTask.EXCEPTION_KEY);
//                getFeedObserver.handleException(ex);
//            }
//        }

        @Override
        protected void handleSuccessMessage(PagedObserver<Status> observer, Bundle data) {
            List<Status> statuses = (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
            observer.handleSuccess(statuses, hasMorePages);
        }
    }

    /**
     * PostStatus
     */

//    public interface SimpleObserver {
//        void handleSuccess();
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void postStatus(AuthToken currUserAuthToken, Status newStatus, SimpleObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                newStatus, new PostStatusHandler(postStatusObserver));
        BackgroundTaskUtils.runTask(statusTask);
    }

    // PostStatusHandler

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
