package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;

import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingRequest;
import edu.byu.cs.tweeter.util.Pair;

public interface IFollowDao {
    boolean create(String followerAlias, String followeeAlias);
    boolean remove(String followerAlias, String followeeAlias);
    Boolean isFollower(String followerAlias, String followeeAlias);
    Pair<List<String>, Boolean> getFollowees(GetFollowingRequest request);
    Pair<List<String>, Boolean> getFollowers(GetFollowersRequest request);
}
