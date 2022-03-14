package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

/**
 * Contains all the information needed to make a request to have the server return the next page of
 * followers for a specified followee.
 */
public class GetFollowersRequest {

    private AuthToken authToken;
    private String followeeAlias;
    private int limit;
    private String lastFollowerAlias;

    private GetFollowersRequest() {}

    public GetFollowersRequest(AuthToken authToken, String followeeAlias, int limit, String lastFollowerAlias) {
        this.authToken = authToken;
        this.followeeAlias = followeeAlias;
        this.limit = limit;
        this.lastFollowerAlias = lastFollowerAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public String getFolloweeAlias() {
        return followeeAlias;
    }

    public void setFolloweeAlias(String followeeAlias) {
        this.followeeAlias = followeeAlias;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getLastFollowerAlias() {
        return lastFollowerAlias;
    }

    public void setLastFollowerAlias(String lastFollowerAlias) {
        this.lastFollowerAlias = lastFollowerAlias;
    }
}
