package edu.byu.cs.tweeter.client.presenter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.net.ServerFacade;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.observer.SimpleObserver;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public class MainPresenterIntegrationTest {

    private ServerFacade serverFacade;
    private MainPresenter.View mockMainView;
    private MainPresenter spyMainPresenter;

    private Cache mockCache;

    private CountDownLatch countDownLatch;

    private User BRIG_USER = new User("Brigham", "Andersen", "@brighamband", "https://tweeter-cs340-profile-pics.s3.us-east-2.amazonaws.com/%40brighamband.png");
    private AuthToken BRIG_UNLI_AUTH_TOKEN = new AuthToken("0bd0dcd3-a2e8-4978-96cf-bedfd00a3795");  // brighamband's auth token that won't expire

    private Status EXPECTED_STATUS = new Status("Integration test", BRIG_USER, "RAND DATE TIME");  // Date time here doesn't matter, the backend generates it


    @Before
    public void setup() {
        serverFacade = new ServerFacade();

        mockMainView = Mockito.mock(MainPresenter.View.class);

        spyMainPresenter = Mockito.spy(new MainPresenter(mockMainView));

        mockCache = Mockito.mock(Cache.class);
        Cache.setInstance(mockCache);
        Mockito.when(mockCache.getCurrUser()).thenReturn(BRIG_USER);
        Mockito.when(mockCache.getCurrUserAuthToken()).thenReturn(BRIG_UNLI_AUTH_TOKEN);

        resetCountDownLatch();
    }

    @Test
    public void integrationTestPostStatus_successful() throws IOException, TweeterRemoteException, InterruptedException {
        // Log in user
        LoginRequest loginRequest = new LoginRequest("@brighamband", "brighamband");
        LoginResponse loginResponse = serverFacade.login(loginRequest, "/login"); // Directly call serverFacade.login()
        assertTrue(loginResponse.isSuccess());
        assertEquals("@brighamband", loginResponse.getUser().getAlias());

        Answer<Void> answer = invocation -> {
            countDownLatch.countDown();
            return null;
        };
        Mockito.doAnswer(answer).when(mockMainView).displayToastMessage(Mockito.anyString());

        // Post a status
        spyMainPresenter.postStatus("Integration test");  // mainPresenter.postStatus() (have mockMainView for presenter)
        awaitCountDownLatch();

        // Verify successfully posted
        Mockito.verify(mockMainView).displayToastMessage(Mockito.anyString());  // Verify "Successfully Posted!"

        // Get user's story
        GetStoryRequest getStoryRequest = new GetStoryRequest(loginResponse.getAuthToken(), loginRequest.getUsername(), 20, null);
        GetStoryResponse getStoryResponse = serverFacade.getStory(getStoryRequest, "/get-story");
        assertTrue(getStoryResponse.isSuccess());
        assertFalse(getStoryResponse.getHasMorePages());
        Status justPostedStatus = getStoryResponse.getStory().get(0);
        assertEquals(EXPECTED_STATUS.getPost(), justPostedStatus.getPost());
        assertEquals(EXPECTED_STATUS.getUser(), justPostedStatus.getUser());
        assertEquals(EXPECTED_STATUS.getUrls(), justPostedStatus.getUrls());
        assertEquals(EXPECTED_STATUS.getMentions(), justPostedStatus.getMentions());
        assertEquals(0, justPostedStatus.getUrls().size());
        assertEquals(0, justPostedStatus.getMentions().size());
    }

    private void resetCountDownLatch() {
        countDownLatch = new CountDownLatch(1);
    }

    private void awaitCountDownLatch() throws InterruptedException {
        countDownLatch.await();
        resetCountDownLatch();
    }
}
