package edu.byu.cs.tweeter.client.model.service.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.service.observer.GetCountObserver;

public class GetCountHandler extends BackgroundTaskHandler<GetCountObserver> {

    public GetCountHandler(GetCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccessMessage(GetCountObserver observer, Bundle data) {
        int count = data.getInt(GetCountTask.COUNT_KEY);
        observer.handleSuccess(count);
    }
}
