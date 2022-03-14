package edu.byu.cs.tweeter.client.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest {
  private MainPresenter.View mockView;
  private StatusService mockStatusService;

  private MainPresenter spyMainPresenter;

  AuthToken TEST_AUTH_TOKEN = new AuthToken("test-auth-token");
  Status TEST_STATUS = new Status("This is my status", null, null, null, null);

  @Before
  public void setup() {
    // Create mocks
    mockView = Mockito.mock(MainPresenter.View.class);
    mockStatusService = Mockito.mock(StatusService.class);

    spyMainPresenter = Mockito.spy(new MainPresenter(mockView));

    Mockito.when(spyMainPresenter.getStatusService()).thenReturn(mockStatusService);

    Cache.getInstance().setCurrUserAuthToken(TEST_AUTH_TOKEN);
  }

  @Test
  public void testPostStatus_postSuccessful() {

    // Create
    Answer<Void> answer = invocation -> {
      MainPresenter.PostStatusObserver observer = setupMockObserver(invocation);

      verifyParameters(invocation); // Confirm

      observer.handleSuccess();
      return null;
    };
    Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

    // Call
    callPostStatus();

    // Confirm
    confirmResults("Successfully Posted!");
  }

  @Test
  public void testPostStatus_postFailedWithMessage() {
    // Create
    Answer<Void> answer = invocation -> {
      MainPresenter.PostStatusObserver observer = setupMockObserver(invocation);

      verifyParameters(invocation); // Confirm

      observer.handleFailure("No wifi connection");
      return null;
    };
    Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

    // Call
    callPostStatus();

    // Confirm
    confirmResults("Failed to post status: No wifi connection");
  }

  @Test
  public void testPostStatus_postFailedWithException() {
    // Create
    Answer<Void> answer = invocation -> {
      MainPresenter.PostStatusObserver observer = setupMockObserver(invocation);

      verifyParameters(invocation); // Confirm

      observer.handleException(new RuntimeException("Runtime exception has occurred"));
      return null;
    };
    Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());

    // Call
    callPostStatus();

    // Confirm
    confirmResults(
        "Failed to post status because of exception: java.lang.RuntimeException: Runtime exception has occurred");
  }

  private MainPresenter.PostStatusObserver setupMockObserver(InvocationOnMock invocation) {
    return invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
  }

  private void callPostStatus() {
    spyMainPresenter.postStatus(TEST_STATUS.getPost());
  }

  private void verifyParameters(InvocationOnMock invocation) {
    Assert.assertEquals(invocation.getArgument(0, AuthToken.class).getToken(), TEST_AUTH_TOKEN.getToken());
    Assert.assertEquals(invocation.getArgument(1, Status.class).getPost(), TEST_STATUS.getPost());
    Assert.assertEquals(invocation.getArgument(2, MainPresenter.PostStatusObserver.class).getMsgPrefix(),
        "Failed to post status");
  }

  private void confirmResults(String msg) {
    Mockito.verify(mockView).displayToastMessage("Posting Status...");
    Mockito.verify(mockView).displayToastMessage(msg);
    Mockito.verify(mockView, Mockito.times(2)).displayToastMessage(Mockito.any());
  }
}
