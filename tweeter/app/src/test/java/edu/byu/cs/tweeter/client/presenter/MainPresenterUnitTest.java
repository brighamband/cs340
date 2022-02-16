package edu.byu.cs.tweeter.client.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.client.TestsWorkingTest;
import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.StatusService;

public class MainPresenterUnitTest {
    private MainPresenter.View mockView;
    private StatusService mockStatusService;
    private Cache mockCache;

    private MainPresenter spyMainPresenter;

    @Before
    public void setup() {
        // Create mocks
        mockView = Mockito.mock(MainPresenter.View.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockCache = Mockito.mock(Cache.class);

        spyMainPresenter = Mockito.spy(new MainPresenter(mockView));
        Mockito.when(spyMainPresenter.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }
//
//    @Test
//    public void testAsserts() {
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
