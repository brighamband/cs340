package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public interface IStoryDao {
    boolean create(String authorAlias, long timestamp, String post);
    Pair<List<Status>, Boolean> getStory(User user, int limit, Long lastTimestamp);
}
