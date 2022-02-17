package edu.byu.cs.tweeter.client.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.TestsWorkingTest;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;

public class MainPresenterUnitTest {
    private MainPresenter.View mockView;
    private UserService mockUserService;
    private StatusService mockStatusService;
    private Cache mockCache;

    private MainPresenter spyMainPresenter;

    @Before
    public void setup() {
        // Create mocks
        mockView = Mockito.mock(MainPresenter.View.class);
        mockUserService = Mockito.mock(UserService.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        spyMainPresenter = Mockito.spy(new MainPresenter(mockView));

        Mockito.when(spyMainPresenter.getUserService()).thenReturn(mockUserService);
        Mockito.when(spyMainPresenter.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }

    @Test
    public void testLogout_logoutSuccessful() {
        // Create
        Answer<Void> answer = invocation -> {
            MainPresenter.LogoutObserver observer = invocation.getArgument(1, MainPresenter.LogoutObserver.class);
            observer.handleSuccess();
            return null;
        };
        Mockito.doAnswer(answer).when(mockUserService).logOut(Mockito.any(), Mockito.any());

        // Call
        spyMainPresenter.logOut();

        // Confirm
        Mockito.verify(mockView).displayToastMessage("Logging Out...");
        Mockito.verify(mockCache).clearCache();
        Mockito.verify(mockView).returnToLoginScreen();
    }

    @Test
    public void testLogout_logoutFailedWithMessage() {

    }

    @Test
    public void testLogout_logoutFailedWithException() {

    }

    @Test
    public void testPostStatus_postSuccessful() {
        // Create
        Answer<Void> answer = invocation -> {
            MainPresenter.PostStatusObserver observer = invocation.getArgument(2, MainPresenter.PostStatusObserver.class);
            observer.handleSuccess();
            return null;
        };
        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(), Mockito.any(), Mockito.any());


        // Call
        spyMainPresenter.postStatus("Testing status post");

        // Confirm
        Mockito.verify(mockView).displayToastMessage("Successfully Posted!");
    }
//
//    @Test
//    public void testPostStatus_postFailedWithMessage() {
//
//    }
//
//    @Test
//    public void testPostStatus_postFailedWithException() {
//
//    }
//        Assert.assertTrue(true);
//    }
//    @Test
//    public void testMockitoSpy() {
//        TestsWorkingTest.Foo f = Mockito.spy(new TestsWorkingTest.Foo());
//        f.foo();
//        Mockito.verify(f).foo();
//    }
//    @Test
//    public void testMockitoMock() {
//        TestsWorkingTest.Foo f = Mockito.mock(TestsWorkingTest.Foo.class);
//        f.foo();
//        Mockito.verify(f).foo();
//    }

}
