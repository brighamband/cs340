package edu.byu.cs.tweeter.server.service;

import java.util.GregorianCalendar;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;

public class Service {
    IDaoFactory daoFactory;

    public final long TOKEN_TIME_TO_LIVE = 3600000;  // 1 hour in ms

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
        long dbTimestamp = daoFactory.getAuthTokenDao().getTimestamp(token);
        if (dbTimestamp == -1) {    // Failure case #1 - not found
            return false;
        }

        // Check expiration
        long currentTimestamp = new GregorianCalendar().getTimeInMillis();
        long expiration = dbTimestamp + TOKEN_TIME_TO_LIVE;
        // If expired, remove
        if (currentTimestamp >= expiration) {
            daoFactory.getAuthTokenDao().remove(token);
            return false;
        }

        // Else (if valid, renew)
        daoFactory.getAuthTokenDao().renewToken(token);
        return true;
    }
}
