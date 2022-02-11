package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.service.observer.BooleanObserver;

public class IsFollowerHandler extends BackgroundTaskHandler<BooleanObserver> {

    public IsFollowerHandler(BooleanObserver isFollowerObserver) {
        super(isFollowerObserver);
    }

    @Override
    protected void handleSuccessMessage(BooleanObserver observer, Bundle data) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }
}