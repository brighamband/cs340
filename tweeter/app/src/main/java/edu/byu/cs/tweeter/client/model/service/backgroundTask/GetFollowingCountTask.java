package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    static final String URL_PATH = "/get-following-count";

    private ServerFacade serverFacade;

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        GetFollowingCountRequest request = new GetFollowingCountRequest(getAuthToken(), getTargetUser());
        GetFollowingCountResponse response = getServerFacade().getFollowingCount(request, URL_PATH);

        setCount(response.getCount());
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, getCount());
    }

    /**
     * Returns an instance of {@link ServerFacade}. Allows mocking of the
     * ServerFacade class for
     * testing purposes. All usages of ServerFacade should get their instance from
     * this method to
     * allow for proper mocking.
     *
     * @return the instance.
     */
    public ServerFacade getServerFacade() {
        if (serverFacade == null) {
            serverFacade = new ServerFacade();
        }
        return new ServerFacade();
    }
}