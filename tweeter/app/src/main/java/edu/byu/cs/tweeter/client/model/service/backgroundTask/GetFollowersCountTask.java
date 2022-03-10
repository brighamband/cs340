package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    static final String URL_PATH = "/get-followers-count";

    private ServerFacade serverFacade;

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(messageHandler, authToken, targetUser);
    }

    @Override
    protected void runTask() throws IOException, TweeterRemoteException {
        GetFollowersCountRequest request = new GetFollowersCountRequest(getAuthToken(), getTargetUser());
        GetFollowersCountResponse response = getServerFacade().getFollowersCount(request, URL_PATH);

        setCount(response.getCount());
    }

    @Override
    protected void loadMessageBundle(Bundle msgBundle) {
        msgBundle.putInt(COUNT_KEY, getCount());
    }

    public ServerFacade getServerFacade() {
        if (serverFacade == null) {
            serverFacade = new ServerFacade();
        }
        return new ServerFacade();
    }
}
