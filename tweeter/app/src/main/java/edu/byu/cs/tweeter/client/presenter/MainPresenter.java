package edu.byu.cs.tweeter.client.presenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.observer.BooleanObserver;
import edu.byu.cs.tweeter.client.model.service.observer.GetCountObserver;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter extends SimplePresenter {
  public interface View extends SimplePresenter.View {
    void returnToLoginScreen();

    void displayFolloweeCount(int count);

    void displayFollowersCount(int count);

    void fetchFollowingAndFollowersCounts();

    void updateFollowButton(boolean currentlyFollowing);

    void setEnabledFollowButton(boolean makeEnabled);
  }

  private final View view;
  private UserService userService;
  private StatusService statusService;
  private final FollowService followService;

  public MainPresenter(View view) {
    super(view);
    this.view = view;
    userService = new UserService();
    followService = new FollowService();
  }

  protected StatusService getStatusService() {
    if (statusService == null) {
      statusService = new StatusService();
    }
    return statusService;
  }

  /**
   * Logout
   */

  public void logOut() {
    userService.logOut(Cache.getInstance().getCurrUserAuthToken(), new LogoutObserver());
  }

  public class LogoutObserver extends Observer implements SimpleObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to logout";
    }

    @Override
    public void handleSuccess() {
      view.returnToLoginScreen();
    }
  }

  /**
   * PostStatus
   */

  public void postStatus(String post) {
    view.displayToastMessage("Posting Status...");
    try {
      Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post),
          parseMentions(post));
      getStatusService().postStatus(Cache.getInstance().getCurrUserAuthToken(), newStatus, new PostStatusObserver());
    } catch (Exception ex) {
      view.displayToastMessage("Failed to post status because of exception: " + ex.getMessage());
    }
  }

  public String getFormattedDateTime() throws ParseException {
    SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

    return statusFormat
        .format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
  }

  public List<String> parseURLs(String post) {
    List<String> containedUrls = new ArrayList<>();
    for (String word : post.split("\\s")) {
      if (word.startsWith("http://") || word.startsWith("https://")) {

        int index = findUrlEndIndex(word);

        word = word.substring(0, index);

        containedUrls.add(word);
      }
    }

    return containedUrls;
  }

  public int findUrlEndIndex(String word) {
    if (word.contains(".com")) {
      int index = word.indexOf(".com");
      index += 4;
      return index;
    } else if (word.contains(".org")) {
      int index = word.indexOf(".org");
      index += 4;
      return index;
    } else if (word.contains(".edu")) {
      int index = word.indexOf(".edu");
      index += 4;
      return index;
    } else if (word.contains(".net")) {
      int index = word.indexOf(".net");
      index += 4;
      return index;
    } else if (word.contains(".mil")) {
      int index = word.indexOf(".mil");
      index += 4;
      return index;
    } else {
      return word.length();
    }
  }

  public List<String> parseMentions(String post) {
    List<String> containedMentions = new ArrayList<>();

    for (String word : post.split("\\s")) {
      if (word.startsWith("@")) {
        word = word.replaceAll("[^a-zA-Z0-9]", "");
        word = "@".concat(word);

        containedMentions.add(word);
      }
    }

    return containedMentions;
  }

  public class PostStatusObserver extends Observer implements SimpleObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to post status";
    }

    @Override
    public void handleSuccess() {
      view.displayToastMessage("Successfully Posted!");
    }
  }

  /**
   * Combined GetFollowingCount and GetFollowersCount
   */
  public void getFollowingAndFollowersCounts(User selectedUser) {
    AuthToken currAuthToken = Cache.getInstance().getCurrUserAuthToken();
    followService.getFollowingCount(currAuthToken, selectedUser, new GetFollowingCountObserver());
    followService.getFollowersCount(currAuthToken, selectedUser, new GetFollowersCountObserver());
  }

  public class GetFollowingCountObserver extends Observer implements GetCountObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to get following count";
    }

    @Override
    public void handleSuccess(int count) {
      view.displayFolloweeCount(count);
    }
  }

  public class GetFollowersCountObserver extends Observer implements GetCountObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to get followers count";
    }

    @Override
    public void handleSuccess(int count) {
      view.displayFollowersCount(count);
    }
  }

  /**
   * Follow / Unfollow
   */

  public void onFollowButtonClick(boolean buttonTextShowsFollowing, User selectedUser) {
    view.setEnabledFollowButton(false);

    if (buttonTextShowsFollowing) {
      view.displayToastMessage("Removing " + selectedUser.getName() + "...");
      unfollow(selectedUser);
    } else {
      view.displayToastMessage("Adding " + selectedUser.getName() + "...");
      follow(selectedUser);
    }
  }

  /**
   * Follow
   */

  public void follow(User selectedUser) {
    followService.follow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new FollowObserver());
  }

  public class FollowObserver extends Observer implements SimpleObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to follow";
    }

    @Override
    public void handleSuccess() {
      // Now that you've followed someone new, re-fetch the counts
      view.fetchFollowingAndFollowersCounts();
      view.updateFollowButton(true);
      // Re-enable the follow button
      view.setEnabledFollowButton(true);
    }

    @Override
    public void handleFailure(String message) {
      view.displayToastMessage(getMsgPrefix() + message);
      // Re-enable the follow button
      view.setEnabledFollowButton(true);
    }

    @Override
    public void handleException(Exception exception) {
      view.displayToastMessage(getMsgPrefix() + "because of exception: " + exception.getMessage());
      // Re-enable the follow button
      view.setEnabledFollowButton(true);
    }
  }

  /**
   * Unfollow
   */

  public void unfollow(User selectedUser) {
    followService.unfollow(Cache.getInstance().getCurrUserAuthToken(), selectedUser, new UnfollowObserver());
  }

  public class UnfollowObserver extends Observer implements SimpleObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to unfollow";
    }

    @Override
    public void handleSuccess() {
      // Now that you've unfollowed someone, re-fetch the counts
      view.fetchFollowingAndFollowersCounts();
      view.updateFollowButton(false);
      // Re-enable the follow button
      view.setEnabledFollowButton(true);
    }

    @Override
    public void handleFailure(String message) {
      view.displayToastMessage(getMsgPrefix() + message);
      // Re-enable the follow button
      view.setEnabledFollowButton(true);
    }

    @Override
    public void handleException(Exception exception) {
      view.displayToastMessage(getMsgPrefix() + "because of exception: " + exception.getMessage());
      // Re-enable the follow button
      view.setEnabledFollowButton(true);
    }
  }

  /**
   * IsFollower
   */

  public void isFollower(User selectedUser) {
    followService.isFollower(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser(),
        selectedUser, new IsFollowerObserver());
  }

  public class IsFollowerObserver extends Observer implements BooleanObserver {
    @Override
    public String getMsgPrefix() {
      return "Failed to determine following relationship";
    }

    @Override
    public void handleSuccess(boolean currentlyFollowing) {
      // If logged in user if a follower of the selected user, display the follow
      // button as "following"
      view.updateFollowButton(currentlyFollowing);
    }
  }
}
