package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Serializable;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends BackgroundTask {
//    private static final String LOG_TAG = "GetFollowingTask";

    public static final String FOLLOWEES_KEY = "followees";
    public static final String MORE_PAGES_KEY = "more-pages";

    private List<User> followees;
    private boolean hasMorePages;

    /**
     * Auth token for logged-in user.
     */
    private AuthToken authToken;
    /**
     * The user whose following is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;
    /**
     * Maximum number of followed users to return (i.e., page size).
     */
    private int limit;
    /**
     * The last person being followed returned in the previous page of results (can be null).
     * This allows the new page to begin where the previous page ended.
     */
    private User lastFollowee;
    /**
     * Message handler that will receive task results.
     */
    private Handler messageHandler;

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler);
        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastFollowee = lastFollowee;
    }

    private Pair<List<User>, Boolean> getFollowees() {
        return getFakeData().getPageOfUsers((User) lastFollowee, limit, targetUser);
    }

    @Override
    protected void runTask() {
//        try {
        Pair<List<User>, Boolean> pageOfUsers = getFollowees();

        followees = pageOfUsers.getFirst();
        hasMorePages = pageOfUsers.getSecond();

        // TODO -- Add custom error message
//        } catch (Exception ex) {
//            Log.e(LOG_TAG, "Failed to get followees", ex);
//            sendExceptionMessage(ex);
//        }
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putSerializable(FOLLOWEES_KEY, (Serializable) followees);
        msgBundle.putBoolean(MORE_PAGES_KEY, hasMorePages);
    }
}
