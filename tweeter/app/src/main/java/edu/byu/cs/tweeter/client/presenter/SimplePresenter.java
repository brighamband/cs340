package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class SimplePresenter {

    public interface View {
        void displayToastMessage(String message);
    }

    private View view;

    public SimplePresenter(View view) {
        this.view = view;
    }

    public abstract class Observer implements ServiceObserver {
        public abstract String getMsgPrefix();

        @Override
        public void handleFailure(String message) {
            view.displayToastMessage(getMsgPrefix() + message);
        }

        @Override
        public void handleException(Exception exception) {
            view.displayToastMessage(getMsgPrefix() + "because of exception: " + exception);
        }
    }
}
