package edu.byu.cs.tweeter.client.presenter;

public abstract class SimplePresenter {

    public interface View {
        void displayToastMessage(String message);
    }

    private View view;

    public SimplePresenter(View view) {
        this.view = view;
    }

    public abstract String getMsgPrefix();
}
