package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedTask<User> {

    static final String URL_PATH = "/get-followers";

    private ServerFacade serverFacade;

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollower);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws IOException, TweeterRemoteException {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        String lastFollowerAlias = getLastItem() == null ? null : getLastItem().getAlias();

        GetFollowersRequest request = new GetFollowersRequest(getAuthToken(), targetUserAlias, getLimit(), lastFollowerAlias);
        GetFollowersResponse response = getServerFacade().getFollowers(request, URL_PATH);

        return new Pair<>(response.getFollowers(), response.getHasMorePages());
//        return getFakeData().getPageOfUsers((User) getLastItem(), getLimit(), getTargetUser());
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
