package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface IAuthTokenDao {
    AuthToken create(String alias);
    long getTimestamp(String token);
    void renewToken(String token);
    void remove(String token);
}
