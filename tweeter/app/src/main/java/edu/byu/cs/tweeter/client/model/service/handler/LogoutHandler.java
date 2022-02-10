package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;

public class LogoutHandler extends BackgroundTaskHandler<SimpleObserver> {

        public LogoutHandler(SimpleObserver logoutObserver) {
            super(logoutObserver);
        }

        @Override
        protected void handleSuccessMessage(SimpleObserver observer, Bundle data) {
            // Clear user data (cached data)
            Cache.getInstance().clearCache();

            observer.handleSuccess();
        }
    }
