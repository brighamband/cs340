package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

public abstract class StatusPresenter extends PagedPresenter<Status> {

    public interface View extends PagedPresenter.View {
        void openLinkInBrowser(String urlLink);
    }

    protected View view;
    protected StatusService statusService;

    public StatusPresenter(View view) {
        super(view);
        this.view = view;
        statusService = new StatusService();
    }

    public void onUserMentionClick(String urlOrAliasLink) {
        if (urlOrAliasLink.contains("http")) {
            view.openLinkInBrowser(urlOrAliasLink);
        } else {
            getUserProfile(urlOrAliasLink);
        }
    }
}
