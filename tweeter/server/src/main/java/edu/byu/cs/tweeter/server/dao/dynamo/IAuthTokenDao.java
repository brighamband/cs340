package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface IAuthTokenDao {
    AuthToken create(String alias, String password);
    AuthToken get(String alias);    // Returns token.  It checks expiration, if expired removes it and returns null.
    AuthToken update(String alias); // Updates expiration of auth token to current time.
    void remove(String alias);
}
