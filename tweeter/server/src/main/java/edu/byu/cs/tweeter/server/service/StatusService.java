package edu.byu.cs.tweeter.server.service;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.request.PostUpdateFeedMsg;
import edu.byu.cs.tweeter.model.net.request.UpdateFeedsMsg;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.TimeUtils;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.util.Pair;

public class StatusService extends Service {

  private final int MAX_NUM_FOLLOWERS = 1000000;

  boolean DO_M4A_WAY_OF_UPDATING_FEEDS = true; // Toggle this boolean to adjust if you want to do the M4A or M4B way

  private final String SQS_POST_UPDATE_FEED_MESSAGES_URL = "https://sqs.us-east-2.amazonaws.com/547858414064/PostStatusQueue";
  private final String SQS_UPDATE_FEEDS_URL = "https://sqs.us-east-2.amazonaws.com/547858414064/UpdateFeedQueue";

  private final int MAX_TO_ADD_TO_UPDATE_QUEUE = 500;

  public StatusService(IDaoFactory daoFactory) {
    super(daoFactory);
  }

  public Response postStatus(PostStatusRequest request) {
    // Validate request
    if (request.getStatus() == null) {
      throw new RuntimeException("[BadRequest] Request missing a status");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      return new Response(false, "Auth token has expired. Log back in again to keep using Tweeter.");
    }

    // New status info
    String authorAlias = request.getStatus().getUser().getAlias();
    long timestamp = TimeUtils.getCurrTimeAsLong();
    String post = request.getStatus().getPost();

    // Step 1 of the posting status process
    // -- Have StoryDao create a new status in Story table (postStatus)
    boolean successful = daoFactory.getStoryDao().create(authorAlias, timestamp, post);
    if (!successful) { // Handle failure case #1
      throw new RuntimeException("[ServerError] Unable to add status to Story table");
    }

    if (DO_M4A_WAY_OF_UPDATING_FEEDS) {  // M4A way
      slowlyButSimplyUpdateFollowerFeeds(request.getAuthToken(), authorAlias, timestamp, post);
    } else {  // M4B way
      sendMsgToQueueToInvokeFirstLambda(authorAlias, timestamp, post);
    }

    // Return Response
    return new Response(true);
  }

  /**
   * Step 2 of the M4A (slower yet simpler way with no queuing and added lambdas) posting status process
   * -- Get all the user's followers, make a new item (representing the new status) in the FeedTable for each follower
   */
  private void slowlyButSimplyUpdateFollowerFeeds(AuthToken authToken, String authorAlias, long timestamp, String post) {
    // Have FollowDao get user's followers
    List<String> followerAliases = daoFactory.getFollowDao().getFollowers(
            new GetFollowersRequest(authToken, authorAlias, MAX_NUM_FOLLOWERS, null)).getFirst();
    // Have FeedDao create that same new status in each feed of user's followers
    for (String viewerAlias : followerAliases) {
      boolean successful = daoFactory.getFeedDao().create(viewerAlias, authorAlias, timestamp, post);
      if (!successful) { // Handle failure case #2
        throw new RuntimeException("[ServerError] Unable to add status to Feed table");
      }
    }
  }

  /**
   * Step 2 of M4B posting status process
   * -- Sends message of the user's story to PostStatusQueue so it triggers PostsUpdateFeedMessages lambda
   */
  private void sendMsgToQueueToInvokeFirstLambda(String authorAlias, long timestamp, String post) {
    AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

    // Make object to hold request data
    PostUpdateFeedMsg postUpdateFeedMsg = new PostUpdateFeedMsg(authorAlias, timestamp, post);
    // Serialize data as JSON
    String msgBody = new Gson().toJson(postUpdateFeedMsg);

    SendMessageRequest msgRequest = new SendMessageRequest()
            .withQueueUrl(SQS_POST_UPDATE_FEED_MESSAGES_URL)
            .withMessageBody(msgBody);

    sqs.sendMessage(msgRequest);
  }

  /**
   * Step 3 of M4B posting status process
   * -- Receives message of user's story, gets their followers.
   * -- It then sends the new feed status messages to UpdateFeedQueue so it triggers UpdateFeeds lambda.
   */
  public void postUpdateFeedMessages(SQSEvent event) {
    System.out.println("At postUpdateFeedMessages");
    if (event.getRecords() == null) {
      System.out.println("Empty or invalid message was sent");
      return;
    }

    for (SQSEvent.SQSMessage msg : event.getRecords()) {
      String body = msg.getBody();
      System.out.println("Body: " + body);

      // Parse data
      PostUpdateFeedMsg incomingMsg = new Gson().fromJson(body, PostUpdateFeedMsg.class);

      // Get all the followers of the author
      Pair<List<String>, Boolean> result = daoFactory.getFollowDao().getFollowers(
              new GetFollowersRequest(null, incomingMsg.getAuthorAlias(), MAX_NUM_FOLLOWERS, null)
      );
      if (result.getFirst() == null || result.getSecond()) return;

      List<String> followerAliases = result.getFirst();

      // 3.  Send 2nd message to UpdateFeedQueue so it triggers UpdateFeeds (updates n user feeds at a time with the new status)

      AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

      List<List<String>> followerAliasBatches = chopped(followerAliases, MAX_TO_ADD_TO_UPDATE_QUEUE);
      for (List<String> followerAliasBatch: followerAliasBatches) {
        // Make object to hold request data
        UpdateFeedsMsg updateFeedsMsg = new UpdateFeedsMsg(
                incomingMsg.getAuthorAlias(), incomingMsg.getTimestamp(), incomingMsg.getPost(), followerAliasBatch);
        // Serialize data as JSON
        String msgBody = new Gson().toJson(updateFeedsMsg);

        SendMessageRequest outgoingMsg = new SendMessageRequest()
                .withQueueUrl(SQS_UPDATE_FEEDS_URL)
                .withMessageBody(msgBody);

        sqs.sendMessage(outgoingMsg);
        System.out.println("Sent msgBody:" + msgBody);
      }
    }
  }

  // Chops a list into non-view sub-lists of length L
  private static <T> List<List<T>> chopped(List<T> list, final int L) {
    List<List<T>> parts = new ArrayList<List<T>>();
    final int N = list.size();
    for (int i = 0; i < N; i += L) {
      parts.add(new ArrayList<T>(
              list.subList(i, Math.min(N, i + L)))
      );
    }
    return parts;
  }

  /**
   * Step 4 of the M4B posting status process
   * -- Receives a message with a batch of feeds to update, then batch writes up to 25 at a time
   */
  public void updateFeeds(SQSEvent event) {
    System.out.println("At updateFeeds");
    if (event.getRecords() == null) {
      System.out.println("Empty or invalid message was sent");
      return;
    }

    for (SQSEvent.SQSMessage msg : event.getRecords()) {
      String body = msg.getBody();
      System.out.println("Body: " + body);

      UpdateFeedsMsg updateFeedsMsg = new Gson().fromJson(body, UpdateFeedsMsg.class);

      // Send to DAO to batch create feed entries
      daoFactory.getFeedDao().batchCreate(updateFeedsMsg);
    }
  }

  public GetStoryResponse getStory(GetStoryRequest request) {
    // Validate request
    if (request.getTargetUserAlias() == null) {
      throw new RuntimeException("[BadRequest] Request missing a target user alias");
    } else if (request.getLimit() <= 0) {
      throw new RuntimeException("[BadRequest] Request missing a positive limit");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      return new GetStoryResponse("Auth token has expired. Log back in again to keep using Tweeter.");
    }

    // Set up response data
    String authorAlias = request.getTargetUserAlias();
    User user = daoFactory.getUserDao().getUser(authorAlias);

    // Convert request timestamp string to a long
    Long lastTimestamp = null;
    if (request.getLastStatus() != null) {
      lastTimestamp = TimeUtils.stringTimeToLong(request.getLastStatus().getDate());
      if (lastTimestamp == -1) {
        throw new RuntimeException("[ServerError] Unable to parse lastTimestamp");
      }
    }

    // Have StoryDao get story data
    Pair<List<Status>, Boolean> result = daoFactory.getStoryDao().getStory(user, request.getLimit(), lastTimestamp);
    List<Status> story = result.getFirst();
    Boolean hasMorePages = result.getSecond();

    // Handle failure
    if (story == null && hasMorePages == null) {
      throw new RuntimeException("[ServerException] GetStory calculation not working properly");
    }

    // Return response
    return new GetStoryResponse(story, hasMorePages);
  }

  public GetFeedResponse getFeed(GetFeedRequest request) {
    // Validate request
    if (request.getTargetUserAlias() == null) {
      throw new RuntimeException("[BadRequest] Request missing a target user alias");
    } else if (request.getLimit() <= 0) {
      throw new RuntimeException("[BadRequest] Request missing a positive limit");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      return new GetFeedResponse("Auth token has expired. Log back in again to keep using Tweeter.");
    }

    // Set up response data
    String authorAlias = request.getTargetUserAlias();
    User user = daoFactory.getUserDao().getUser(authorAlias);

    // Convert request timestamp string to a long
    Long lastTimestamp = null;
    if (request.getLastStatus() != null) {
      lastTimestamp = TimeUtils.stringTimeToLong(request.getLastStatus().getDate());
      if (lastTimestamp == -1) {
        throw new RuntimeException("[ServerError] Unable to parse lastTimestamp");
      }
    }

    // Have FeedDao get feed data
    Pair<List<Status>, Boolean> result = daoFactory.getFeedDao().getFeed(user.getAlias(), request.getLimit(), lastTimestamp);
    List<Status> feed = result.getFirst();
    Boolean hasMorePages = result.getSecond();

    // Handle failure
    if (feed == null && hasMorePages == null) {
      throw new RuntimeException("[ServerException] GetStory calculation not working properly");
    }

    // Fill in missing user data for each status
    for (Status status : feed) {
      User completeUser = daoFactory.getUserDao().getUser(status.getUser().getAlias());
      status.setUser(completeUser);
    }

    // Return response
    return new GetFeedResponse(feed, hasMorePages);
  }
}
