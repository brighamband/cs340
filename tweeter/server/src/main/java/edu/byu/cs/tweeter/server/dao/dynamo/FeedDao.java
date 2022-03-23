package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class FeedDao implements IFeedDao {

    @Override
    public List<Status> get(String username, int limit, Status lastStatus) {
        return null;
    }
}
