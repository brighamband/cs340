package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.util.Pair;

public interface IFeedDao {
    boolean create(String viewerAlias, long timestamp, String post, String authorAlias);
    Pair<List<Status>, Boolean> getFeed(String viewerAlias, int limit, Long lastTimestamp);
}
