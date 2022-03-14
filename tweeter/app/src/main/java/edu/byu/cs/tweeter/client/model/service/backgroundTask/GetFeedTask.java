package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedTask<Status> {

    static final String URL_PATH = "/get-feed";

    private ServerFacade serverFacade;

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastStatus);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() throws IOException, TweeterRemoteException {
        GetFeedRequest request = new GetFeedRequest(getAuthToken(), getTargetUser().getAlias(), getLimit(), getLastItem());
        GetFeedResponse response = getServerFacade().getFeed(request, URL_PATH);

        return new Pair<>(response.getFeed(), response.getHasMorePages());
    }

    public ServerFacade getServerFacade() {
        if (serverFacade == null) {
            serverFacade = new ServerFacade();
        }
        return new ServerFacade();
    }
}
