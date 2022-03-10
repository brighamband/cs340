package edu.byu.cs.tweeter.client.model.service.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedTask<User> {

    static final String URL_PATH = "/get-following";

    private ServerFacade serverFacade;

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(messageHandler, authToken, targetUser, limit, lastFollowee);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() throws IOException, TweeterRemoteException {
        String targetUserAlias = getTargetUser() == null ? null : getTargetUser().getAlias();
        String lastFolloweeAlias = getLastItem() == null ? null : getLastItem().getAlias();

        GetFollowingRequest request = new GetFollowingRequest(getAuthToken(), targetUserAlias, getLimit(), lastFolloweeAlias);
        GetFollowingResponse response = getServerFacade().getFollowees(request, URL_PATH);

        return new Pair<>(response.getFollowees(), response.getHasMorePages());
//        return new getPageOfUsers((User) getLastItem(), getLimit(), getTargetUser())
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
