package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.handler.BackgroundTaskHandler;
import edu.byu.cs.tweeter.client.model.service.handler.GetCountHandler;
import edu.byu.cs.tweeter.client.model.service.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleHandler;
import edu.byu.cs.tweeter.client.model.service.observer.BooleanObserver;
import edu.byu.cs.tweeter.client.model.service.observer.GetCountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {
    /**
     * GetFollowing
     */

//    public interface GetFollowingObserver extends PagedObserver<User> {
//        void handleSuccess(List<User> following, boolean hasMorePages);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void getFollowing(AuthToken currUserAuthToken, User user, int pageSize, User lastFollowee,
                             PagedObserver<User> getFollowingObserver) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(currUserAuthToken,
                user, pageSize, lastFollowee, new PagedHandler<User>(getFollowingObserver));
        BackgroundTaskUtils.runTask(getFollowingTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
//    private class GetFollowingHandler extends BackgroundTaskHandler<PagedObserver<User>> {
//        private PagedObserver<User getFollowingObserver;

//        public GetFollowingHandler(PagedObserver<User> getFollowingObserver) {
//            super(getFollowingObserver);
//            this.getFollowingObserver = getFollowingObserver;
//        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
//            if (success) {
//                List<User> following = (List<User>) msg.getData().getSerializable(GetFollowingTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);
//                getFollowingObserver.handleSuccess(following, hasMorePages);
//            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
//                getFollowingObserver.handleFailure(message);
//            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
//                getFollowingObserver.handleException(ex);
//            }
//        }

//        @Override
//        protected void handleSuccessMessage(PagedObserver<User> observer, Bundle data) {
//            List<User> following = (List<User>) data.getSerializable(GetFollowingTask.ITEMS_KEY);
//            boolean hasMorePages = data.getBoolean(GetFollowingTask.MORE_PAGES_KEY);
//            observer.handleSuccess(following, hasMorePages);
//        }
//    }

    /**
     * GetFollowingCount
     */

//    public interface GetCountObserver extends GetCountObserver {
//        void handleSuccess(int count);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void getFollowingCount(AuthToken currUserAuthToken, User selectedUser,
                                  GetCountObserver observer) {
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(observer));
        BackgroundTaskUtils.runTask(followingCountTask);
    }

    // GetFollowingCountHandler

//    private class GetFollowingCountHandler extends Handler {
//        private GetCountObserver observer;
//
//        public GetFollowingCountHandler(GetCountObserver observer) {
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
//            if (success) {
//                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
//                observer.handleSuccess(count);
//            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
//                observer.handleFailure(message);
//            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
//                observer.handleException(ex);
//            }
//        }
//    }

    /**********************************************************************************************
     * GetFollowers      // FIXME - DUPLICATED (following/followers implementation from above)
     */

//    public interface PagedObserver<User extends PagedObserver<User> {
//        void handleSuccess(List<User> followers, boolean hasMorePages);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void getFollowers(AuthToken currUserAuthToken, User user, int pageSize, User lastFollower,
                             PagedObserver<User> getFollowersObserver) {
        GetFollowersTask getFollowersTask = new GetFollowersTask(currUserAuthToken,
                user, pageSize, lastFollower, new PagedHandler<User>(getFollowersObserver));
        BackgroundTaskUtils.runTask(getFollowersTask);
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
//    private class GetFollowersHandler extends Handler {
//        private PagedObserver<User> observer;
//
//        public GetFollowersHandler(PagedObserver<User> observer) {
//            this.observer = observer;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
//            if (success) {
//                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.ITEMS_KEY);
//                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
//                observer.handleSuccess(followers, hasMorePages);
//            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
//                observer.handleFailure(message);
//            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
//                observer.handleException(ex);
//            }
//        }
//    }

    /**
     * GetFollowersCount
     */

//    public interface GetCountObserver extends GetCountObserver {
//        void handleSuccess(int count);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void getFollowersCount(AuthToken currUserAuthToken, User selectedUser,
                                  GetCountObserver observer) {
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(currUserAuthToken,
                selectedUser, new GetCountHandler(observer));
        BackgroundTaskUtils.runTask(followersCountTask);
    }

    // GetFollowersCountHandler

//    private class GetFollowersCountHandler extends Handler {
//        private GetCountObserver observer;
//
//        public GetFollowersCountHandler(GetCountObserver observer) {
//            this.observer = observer;
//        }
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
//            if (success) {
//                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
//                observer.handleSuccess(count);
//            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
//                observer.handleFailure(message);
//            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
//                observer.handleException(ex);
//            }
//        }
//    }

    /**
     * Combined GetFollowingCount and GetFollowersCount
     */
    public void getFollowingAndFollowersCounts(
            AuthToken currUserAuthToken, User selectedUser,
            GetCountObserver getFollowingCountObserver,
            GetCountObserver getFollowersCountObserver
    ) {
          getFollowingCount(currUserAuthToken, selectedUser, getFollowingCountObserver);
          getFollowersCount(currUserAuthToken, selectedUser, getFollowersCountObserver);
    }

    /**
     * Follow
     */

//    public interface SimpleObserver {
//        void handleSuccess();
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void follow(AuthToken currUserAuthToken, User selectedUser, SimpleObserver followObserver) {
        FollowTask followTask = new FollowTask(currUserAuthToken,
                selectedUser, new SimpleHandler(followObserver));
        BackgroundTaskUtils.runTask(followTask);
    }

    // FollowHandler

//    private class FollowHandler extends BackgroundTaskHandler<SimpleObserver> {
//        private SimpleObserver followObserver;

//        public FollowHandler(SimpleObserver followObserver) {
//            super(followObserver);
//            this.followObserver = followObserver;
//        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
//            if (success) {
//                followObserver.handleSuccess();
//            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
//                followObserver.handleFailure(message);
//            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
//                followObserver.handleException(ex);
//            }
//        }

//        @Override
//        protected void handleSuccessMessage(SimpleObserver observer, Bundle data) {
//            observer.handleSuccess();
//        }
//    }

    /**
     * Unfollow
     */

//    public interface SimpleObserver {
//        void handleSuccess();
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void unfollow(AuthToken currUserAuthToken, User selectedUser, SimpleObserver unfollowObserver) {
        UnfollowTask unfollowTask = new UnfollowTask(currUserAuthToken,
                selectedUser, new SimpleHandler(unfollowObserver));
        BackgroundTaskUtils.runTask(unfollowTask);
    }

    // UnfollowHandler

//    private class UnfollowHandler extends Handler {
//        private SimpleObserver unfollowObserver;
//
//        public UnfollowHandler(SimpleObserver unfollowObserver) {
//            this.unfollowObserver = unfollowObserver;
//        }
//
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
//            if (success) {
//                unfollowObserver.handleSuccess();
//            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
//                unfollowObserver.handleFailure(message);
//            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
//                unfollowObserver.handleException(ex);
//            }
//        }
//    }

    /**
     * IsFollower
     */

//    public interface BooleanObserver.java {
//        void handleSuccess(boolean currentlyFollowing);
//        void handleFailure(String message);
//        void handleException(Exception exception);
//    }

    public void isFollower(AuthToken currUserAuthToken, User currentUser, User selectedUser, BooleanObserver isFollowerObserver) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(currUserAuthToken, currentUser,
                selectedUser, new IsFollowerHandler(isFollowerObserver));
        BackgroundTaskUtils.runTask(isFollowerTask);
    }

    // IsFollowerHandler

    private class IsFollowerHandler extends BackgroundTaskHandler<BooleanObserver> {
//        private BooleanObserver isFollowerObserver;

        public IsFollowerHandler(BooleanObserver isFollowerObserver) {
            super(isFollowerObserver);
//            this.isFollowerObserver = isFollowerObserver;
        }

//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
//            if (success) {
//                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
//                isFollowerObserver.handleSuccess(isFollower);
//            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
//                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
//                isFollowerObserver.handleFailure(message);
//            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
//                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
//                isFollowerObserver.handleException(ex);
//            }
//        }

        @Override
        protected void handleSuccessMessage(BooleanObserver observer, Bundle data) {
            boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
            observer.handleSuccess(isFollower);
        }
    }
}
