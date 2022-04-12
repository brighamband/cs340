package edu.byu.cs.tweeter.server.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import edu.byu.cs.tweeter.server.dao.dynamo.StoryDao;

public class StatusServiceTest {

    private StoryDao mockStoryDao;
    private StatusService statusServiceSpy;

    @Before
    public void setup() {
        mockStoryDao = Mockito.mock(StoryDao.class);
    }

    @Test
    public void testGetStory_successful() {

    }

    @Test
    public void integrationTestPostStatus_successful() {
        // Directly call serverFacade.login()
        // mainPresenter.postStatus() (have mockview for presenter)
        // serverFacade.getStory() - Make sure most recent post matches
        // Make
    }
}
