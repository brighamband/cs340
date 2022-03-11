package edu.byu.cs.tweeter.client.model.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.request.GetFollowersRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersResponse;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;

/**
 * Integration tests to verify Server Facade class correctly communicates with server
 */
public class ServerFacadeTest {

    private FakeData fakeData;

    private ServerFacade serverFacade;

    @Before
    public void setup() {
        fakeData = new FakeData();
        serverFacade = new ServerFacade();
    }

    @Test
    public void testRegister_successful() throws IOException, TweeterRemoteException {
        User testUser = fakeData.getFirstUser();
        RegisterRequest validRequest = new RegisterRequest(testUser.getFirstName(), testUser.getLastName(), testUser.getAlias(), "password", testUser.getImageUrl());

        RegisterResponse response = serverFacade.register(validRequest, "/register");

        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
        assertNotNull(response.getAuthToken());
        assertNotNull(response.getUser());
        assertEquals(testUser, response.getUser());
    }

    @Test
    public void testRegister_unsuccessful() throws IOException, TweeterRemoteException {
        User testUser = fakeData.getFirstUser();
        RegisterRequest noUsernameRequest = new RegisterRequest(
                testUser.getFirstName(), testUser.getLastName(), null, "password", testUser.getImageUrl());

        RegisterResponse response = serverFacade.register(noUsernameRequest, "/register");

        assertFalse(response.isSuccess());
        assertNull(response.getUser());
        assertNull(response.getAuthToken());
    }

    @Test
    public void testGetFollowers_successful() throws IOException, TweeterRemoteException {
        GetFollowersRequest validRequest = new GetFollowersRequest(
                new AuthToken("token", "datetime"), "@followee", 10, "@follower");

        GetFollowersResponse response = serverFacade.getFollowers(validRequest, "/get-followers");

        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
        assertNotNull(response.getFollowers());
    }

    @Test
    public void testGetFollowers_unsuccessful() throws IOException, TweeterRemoteException {
        GetFollowersRequest noAliasRequest = new GetFollowersRequest(
                new AuthToken("token", "datetime"), null, 10, "@follower");

        GetFollowersResponse response = serverFacade.getFollowers(noAliasRequest, "/get-followers");

        assertFalse(response.isSuccess());
        assertNull(response.getFollowers());
    }

    @Test
    public void testGetFollowingCount_successful() throws IOException, TweeterRemoteException {
        User testUser = fakeData.getFirstUser();
        GetFollowingCountRequest validRequest = new GetFollowingCountRequest(
                new AuthToken("token", "datetime"),
                new User(testUser.getFirstName(), testUser.getLastName(), testUser.getImageUrl()));

        GetFollowingCountResponse response = serverFacade.getFollowingCount(validRequest, "/get-following-count");

        assertTrue(response.isSuccess());
        assertNull(response.getMessage());
        assertNotNull(response.getCount());
    }

    @Test
    public void testGetFollowingCount_unsuccessful() throws IOException, TweeterRemoteException {
        GetFollowingCountRequest noUserRequest = new GetFollowingCountRequest(
                new AuthToken("token", "datetime"), null);

        GetFollowingCountResponse response = serverFacade.getFollowingCount(noUserRequest, "/get-following-count");

        assertFalse(response.isSuccess());
        assertEquals(0, response.getCount());
    }
}
