package edu.byu.cs.tweeter.client.model.service;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTaskUtils;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.service.handler.SimpleHandler;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StatusService {
    public void getStory(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus,
                          PagedObserver<Status> getStoryObserver) {
        GetStoryTask getStoryTask = new GetStoryTask(currUserAuthToken,
                user, pageSize, lastStatus, new PagedHandler<Status>(getStoryObserver));
        BackgroundTaskUtils.runTask(getStoryTask);
    }

    public void getFeed(AuthToken currUserAuthToken, User user, int pageSize, Status lastStatus,
                        PagedObserver<Status> getFeedObserver) {
        GetFeedTask getFeedTask = new GetFeedTask(currUserAuthToken,
                user, pageSize, lastStatus, new PagedHandler<Status>(getFeedObserver));
        BackgroundTaskUtils.runTask(getFeedTask);
    }

    public void postStatus(AuthToken currUserAuthToken, Status newStatus, SimpleObserver postStatusObserver) {
        PostStatusTask statusTask = new PostStatusTask(currUserAuthToken,
                newStatus, new SimpleHandler(postStatusObserver));
        BackgroundTaskUtils.runTask(statusTask);
    }
}
