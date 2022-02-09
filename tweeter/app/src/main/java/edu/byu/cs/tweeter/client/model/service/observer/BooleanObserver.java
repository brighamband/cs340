package edu.byu.cs.tweeter.client.model.service.observer;

public interface BooleanObserver extends ServiceObserver {
    void handleSuccess(boolean isTrue);
}
