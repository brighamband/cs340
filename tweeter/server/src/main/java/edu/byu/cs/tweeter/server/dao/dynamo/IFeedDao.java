package edu.byu.cs.tweeter.server.dao.dynamo;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public interface IFeedDao {
    List<Status> get(String username, int limit, Status lastStatus);
}
