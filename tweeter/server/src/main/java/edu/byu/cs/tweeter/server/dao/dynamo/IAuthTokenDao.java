package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface IAuthTokenDao {
    AuthToken create(String alias, String password);
    AuthToken get(String alias);    // Checks expiration, if expired removes it and returns null.  If not expired, updates the expiration.
    void remove(String alias);
}
