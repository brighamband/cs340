package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class FollowPresenter extends PagedPresenter<User> {

    protected FollowService followService;

    public FollowPresenter(View view) {
        super(view);
        followService = new FollowService();
    }
}
