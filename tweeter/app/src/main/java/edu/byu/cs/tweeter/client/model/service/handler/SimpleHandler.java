package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;

public class SimpleHandler extends BackgroundTaskHandler<SimpleObserver> {

    public SimpleHandler(SimpleObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(SimpleObserver observer, Bundle data) {
        observer.handleSuccess();
    }
}
