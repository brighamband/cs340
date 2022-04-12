package edu.byu.cs.tweeter.server.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.dynamo.AuthTokenDao;
import edu.byu.cs.tweeter.server.dao.dynamo.DynamoDaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.IAuthTokenDao;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.server.dao.dynamo.IFeedDao;
import edu.byu.cs.tweeter.server.dao.dynamo.IFollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.IStoryDao;
import edu.byu.cs.tweeter.server.dao.dynamo.IUserDao;
import edu.byu.cs.tweeter.server.dao.dynamo.StoryDao;
import edu.byu.cs.tweeter.server.dao.dynamo.UserDao;
import edu.byu.cs.tweeter.util.Pair;

public class StatusServiceTest {

    private AuthTokenDao mockAuthTokenDao;
    private UserDao mockUserDao;
    private StoryDao mockStoryDao;
    private StatusService statusServiceSpy;

    private GetStoryRequest requestNoPagination;
    private User brigUser;
    private Status brigExpectedStatus;

    private GetStoryRequest requestWithPagination;
    private User jimmerUser;
    private List<Status> jimmerExpectedStatuses;

    @Before
    public void setup() {
        // Set up request

        AuthToken brigUnliAuthToken = new AuthToken("0bd0dcd3-a2e8-4978-96cf-bedfd00a3795");  // brighamband's auth token that won't expire
        brigUser = new User("Brigham", "Andersen", "@brighamband", "https://tweeter-cs340-profile-pics.s3.us-east-2.amazonaws.com/%40brighamband.png");
        requestNoPagination = new GetStoryRequest(brigUnliAuthToken, "@brighamband", 20, null);

        jimmerUser = new User("Jimmer", "Fredette", "@jimmer", "https://tweeter-cs340-profile-pics.s3.us-east-2.amazonaws.com/%40jimmer.png");
        jimmerExpectedStatuses = new ArrayList<>();
        jimmerExpectedStatuses.add(new Status("Post 1", jimmerUser, "Tue Apr 12 19:55:34 UTC 2022"));
        jimmerExpectedStatuses.add(new Status("Post 2", jimmerUser, "Tue Apr 12 19:55:45 UTC 2022"));
        jimmerExpectedStatuses.add(new Status("Post 3", jimmerUser, "Tue Apr 12 19:56:27 UTC 2022"));
        requestWithPagination = new GetStoryRequest(brigUnliAuthToken, jimmerUser.getAlias(), 1, jimmerExpectedStatuses.get(0));    // Requesting posts 2 and 3

        // Set up response

        mockAuthTokenDao = Mockito.mock(AuthTokenDao.class);
        Mockito.when(mockAuthTokenDao.getExpiration(Mockito.any())).thenReturn(1689263064L);

        mockUserDao = Mockito.mock(UserDao.class);
        Mockito.when(mockUserDao.getUser(brigUser.getAlias())).thenReturn(brigUser);
        Mockito.when(mockUserDao.getUser(jimmerUser.getAlias())).thenReturn(jimmerUser);

        // Set up

        mockStoryDao = Mockito.mock(StoryDao.class);
        List<Status> statusList = new ArrayList<>();
        brigExpectedStatus = new Status(
                "Here's a post",
                brigUser,
                "Wed Apr 06 16:36:41 UTC 2022");
        statusList.add(brigExpectedStatus);
        Mockito.when(mockStoryDao.getStory(brigUser, 20, null)).thenReturn(new Pair<>(statusList, false));
        List<Status> statusListSize1ToReturn = new ArrayList<>();
        statusListSize1ToReturn.add(jimmerExpectedStatuses.get(1));
        Mockito.when(mockStoryDao.getStory(jimmerUser, 1, 1649793334L)).thenReturn(new Pair<>(statusListSize1ToReturn, true));

        statusServiceSpy = Mockito.spy(new StatusService(new TestDaoFactory()));
    }

    class TestDaoFactory implements IDaoFactory {

        @Override
        public IUserDao getUserDao() {
            return mockUserDao;
        }

        @Override
        public IAuthTokenDao getAuthTokenDao() {
            return mockAuthTokenDao;
        }

        @Override
        public IFollowDao getFollowDao() {
            return null;
        }

        @Override
        public IStoryDao getStoryDao() {
            return mockStoryDao;
        }

        @Override
        public IFeedDao getFeedDao() {
            return null;
        }
    }

    @Test
    public void testGetStoryNoPagination_successful() {
        GetStoryResponse actualResponse = statusServiceSpy.getStory(requestNoPagination);
        assertTrue(actualResponse.isSuccess());
        assertFalse(actualResponse.getHasMorePages());
        assertEquals(1, actualResponse.getStory().size());
        Status actualStory = actualResponse.getStory().get(0);
        assertEquals(brigUser, actualStory.getUser());
        assertEquals(brigExpectedStatus.getPost() , actualStory.getPost());
        assertEquals(brigExpectedStatus.getMentions() , actualStory.getMentions());
        assertEquals(brigExpectedStatus.getUrls(), actualStory.getUrls());
        assertEquals(brigExpectedStatus.getDate(), actualStory.getDate());
    }

    @Test
    public void testGetStoryWithPagination_successful() {
        GetStoryResponse actualResponse = statusServiceSpy.getStory(requestWithPagination);
        assertTrue(actualResponse.isSuccess());
        assertTrue(actualResponse.getHasMorePages());
        Status actualStory = actualResponse.getStory().get(0);
        assertEquals(jimmerUser, actualStory.getUser());
        assertEquals(jimmerExpectedStatuses.get(1).getPost() , actualStory.getPost());
        assertEquals(jimmerExpectedStatuses.get(1).getMentions() , actualStory.getMentions());
        assertEquals(jimmerExpectedStatuses.get(1).getUrls(), actualStory.getUrls());
        assertEquals(jimmerExpectedStatuses.get(1).getDate(), actualStory.getDate());
//
    }
    // Add pagination test
}
