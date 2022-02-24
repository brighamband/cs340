package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends StatusPresenter {
  public StoryPresenter(View view) {
    super(view);
  }

  @Override
  public void getItems(AuthToken authToken, User targetUser, int pageSize, Status lastItem) {
    statusService.getStory(authToken, targetUser, pageSize, lastItem, new GetStoryObserver());
  }

  public class GetStoryObserver extends PagedListObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to get story";
    }
  }
}
