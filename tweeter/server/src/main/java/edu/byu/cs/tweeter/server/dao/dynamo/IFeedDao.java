package edu.byu.cs.tweeter.server.dao.dynamo;

public interface IFeedDao {
    boolean create(String viewerAlias, long timestamp, String post, String authorAlias);
//    List<Status> get(String username, int limit, Status lastStatus);
}
