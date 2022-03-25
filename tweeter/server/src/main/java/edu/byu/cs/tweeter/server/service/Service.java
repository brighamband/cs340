package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.server.TimeUtils;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;

public class Service {
    IDaoFactory daoFactory;

    public Service(IDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * Empty constructor just for mock tests.
     */
    public Service() {}

    public boolean validateAuthToken(String token) {
        System.out.println("Validating auth token for " + token);

        // Get auth token timestamp from database
        long expiration = daoFactory.getAuthTokenDao().getExpiration(token);
        if (expiration == -1) {    // Failure case #1 - not found
            return false;
        }

        // Check expiration
        long currTimestamp = TimeUtils.getCurrTimeAsLong();
        // If expired
        if (currTimestamp >= expiration) {
//            daoFactory.getAuthTokenDao().remove(token);   // Perhaps you may want to remove old ones eventually
            return false;
        }

        // Else (if valid, renew)
        daoFactory.getAuthTokenDao().renewToken(token);
        return true;
    }
}
