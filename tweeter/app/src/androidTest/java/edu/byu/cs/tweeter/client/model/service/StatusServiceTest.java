package edu.byu.cs.tweeter.client.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * Integration test(s) for Status Service (client-side)
 */
public class StatusServiceTest {

  private StatusService statusService;
  private StatusServiceObserver observer;

  private CountDownLatch countDownLatch;

  private ServerFacade serverFacade;

  @Before
  public void setup() {
    statusService = new StatusService();
    observer = new StatusServiceObserver();

    serverFacade = new ServerFacade();

    resetCountDownLatch();
  }

  private class StatusServiceObserver implements PagedObserver<Status> {

    private boolean success;
    private String message;
    private List<Status> story;
    private boolean hasMorePages;
    private Exception exception;

    @Override
    public void handleSuccess(List<Status> story, boolean hasMorePages) {
      this.success = true;
      this.message = null;
      this.story = story;
      this.hasMorePages = hasMorePages;
      this.exception = null;

      countDownLatch.countDown();
    }

    @Override
    public void handleFailure(String message) {
      this.success = false;
      this.message = message;
      this.story = null;
      this.hasMorePages = false;
      this.exception = null;

      countDownLatch.countDown();
    }

    @Override
    public void handleException(Exception exception) {
      this.success = false;
      this.message = null;
      this.story = null;
      this.hasMorePages = false;
      this.exception = exception;

      countDownLatch.countDown();
    }

    public boolean isSuccess() {
      return success;
    }

    public String getMessage() {
      return message;
    }

    public List<Status> getStory() {
      return story;
    }

    public boolean getHasMorePages() {
      return hasMorePages;
    }

    public Exception getException() {
      return exception;
    }
  }

  private void resetCountDownLatch() {
    countDownLatch = new CountDownLatch(1);
  }

  private void awaitCountDownLatch() throws InterruptedException {
    countDownLatch.await();
    resetCountDownLatch();
  }

  @Test
  public void testGetStory_invalidRequest() throws InterruptedException {
    statusService.getStory(
        null,
        null,
        0,
        null,
        observer);
    awaitCountDownLatch();

    assertFalse(observer.isSuccess());
    assertNull(observer.getMessage());
    assertNull(observer.getStory());
    assertFalse(observer.getHasMorePages());
    assertNotNull(observer.getException());
  }
}
