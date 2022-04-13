package edu.byu.cs.tweeter.client.model.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.model.service.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.FakeData;

/**
 * Integration test(s) for Status Service (client-side)
 */
public class StatusServiceTest {

  private FakeData fakeData;
  private User currUser;
  private AuthToken currAuthToken;

  private StatusService statusService;
  private StatusServiceObserver observer;

  private CountDownLatch countDownLatch;

  @Before
  public void setup() {
    fakeData = new FakeData();
    currUser = new User("FirstName", "LastName", null);
    currAuthToken = new AuthToken();

    statusService = new StatusService();

    observer = new StatusServiceObserver();

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
  public void testGetStory_positivePageSize() throws InterruptedException {
    statusService.getStory(
        currAuthToken,
        currUser,
        5,
        null,
        observer);
    awaitCountDownLatch();

    List<Status> expectedStory = new FakeData().getFakeStatuses().subList(0, 5);
    assertTrue(observer.isSuccess());
    assertNull(observer.getMessage());
    assertEquals(expectedStory.size(), observer.getStory().size());
    for (int i = 0; i < expectedStory.size(); i++) {
      Status expected = expectedStory.get(i);
      Status actual = observer.getStory().get(i);
      assertEquals(expected.getPost(), actual.getPost());
      assertEquals(expected.getUser(), actual.getUser());
      assertEquals(expected.getUrls(), actual.getUrls());
      assertEquals(expected.getMentions(), actual.getMentions());
    }
    assertTrue(observer.getHasMorePages());
    assertNull(observer.getException());
  }

  @Test
  public void testGetStory_pageSizeZero() throws InterruptedException {
    statusService.getStory(
        currAuthToken,
        currUser,
        0,
        null,
        observer);
    awaitCountDownLatch();

    assertTrue(observer.isSuccess());
    assertNull(observer.getMessage());
    assertNull(observer.getStory());
    assertFalse(observer.getHasMorePages());
    assertNull(observer.getException());
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