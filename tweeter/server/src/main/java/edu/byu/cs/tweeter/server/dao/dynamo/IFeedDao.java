package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.UpdateFeedsMsg;
import edu.byu.cs.tweeter.util.Pair;

public interface IFeedDao {
    void batchCreate(UpdateFeedsMsg msg);
    boolean create(String viewerAlias, String authorAlias, long timestamp, String post);
    Pair<List<Status>, Boolean> getFeed(String viewerAlias, int limit, Long lastTimestamp);
}
