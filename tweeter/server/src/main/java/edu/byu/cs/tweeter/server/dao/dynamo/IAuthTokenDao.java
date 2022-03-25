package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface IAuthTokenDao {
    AuthToken create(String alias);
    String getCurrUserAlias(String token);
    long getExpiration(String token);
    void renewToken(String token);
}
