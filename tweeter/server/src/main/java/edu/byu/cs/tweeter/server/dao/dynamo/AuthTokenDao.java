package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class AuthTokenDao implements IAuthTokenDao {
    @Override
    public AuthToken create(String alias, String password) {
        return null;
    }

    @Override
    public AuthToken get(String alias) {
        return null;
    }

    @Override
    public void remove(String alias) {

    }
}
