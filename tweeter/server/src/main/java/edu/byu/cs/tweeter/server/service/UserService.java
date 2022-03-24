package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowersCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
import edu.byu.cs.tweeter.server.dao.dynamo.FollowDao;
import edu.byu.cs.tweeter.server.dao.dynamo.IDaoFactory;
import edu.byu.cs.tweeter.server.dao.s3.IS3Factory;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {
    IDaoFactory daoFactory;
    IS3Factory s3Factory;

    public UserService(IDaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public UserService(IDaoFactory daoFactory, IS3Factory s3Factory) {
        this.daoFactory = daoFactory;
        this.s3Factory = s3Factory;
    }

    public LoginResponse login(LoginRequest request) {
        // Validate request
        if (request.getUsername() == null){
            throw new RuntimeException("[BadRequest] Request missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Request missing a password");
        }

        // Have UserDao check if User exists with that username
        User existingUser = daoFactory.getUserDao().getUser(request.getUsername());
        if (existingUser == null) {
            return new LoginResponse("No users exist with that username");
        }

        // Check password
        String dbHashedPassword = daoFactory.getUserDao().getHashedPassword(request.getUsername());
//        String unhashedPassword = unHash(dbHashedPassword);
        // FIXME - Passwords need hashed
        String unhashedPassword = dbHashedPassword;
        if (!request.getPassword().equals(unhashedPassword)) {  // If passwords don't match
            return new LoginResponse("Invalid password for " + request.getUsername());
        }

        // Have AuthTokenDao update the AuthToken for the user
        AuthToken updatedAuthToken = daoFactory.getAuthTokenDao().update(request.getUsername());

        // Return response
        return new LoginResponse(existingUser, updatedAuthToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        // Validate request
        if (request.getUsername() == null) {
            throw new RuntimeException("[BadRequest] Request missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Request missing a password");
        } else if (request.getFirstName() == null) {
            throw new RuntimeException("[BadRequest] Request missing a first name");
        } else if (request.getLastName() == null) {
            throw new RuntimeException("[BadRequest] Request missing a last name");
        }else if (request.getImage() == null) {
            throw new RuntimeException("[BadRequest] Request missing an image");
        }

        // Have UserDao check to see if a user already exists with that username
        User existingUser = daoFactory.getUserDao().getUser(request.getUsername());
        if (existingUser != null) {
            return new RegisterResponse("A user already exists with that username");
        }

        System.out.println("Starting image upload");

        // Have S3Dao upload image to S3
//        String imageUrl = s3Factory.getS3Dao().uploadImage(request.getUsername(), request.getImage());
        String imageUrl = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

        System.out.println("About to create in UserDao");

        // Have UserDao to create (register) a new user
        User newUser = daoFactory.getUserDao().create(
                request.getFirstName(), request.getLastName(),
                request.getUsername(), imageUrl);
        if (newUser == null) {
            return new RegisterResponse("Failed to register new user");
        }

        // Get an auth token from the AuthTokenDao
        AuthToken newAuthToken = daoFactory.getAuthTokenDao()
                .create(newUser.getAlias(), request.getPassword());

        // Return RegisterResponse
        return new RegisterResponse(newUser, newAuthToken);
    }

    public Response logout() {
        // TODO: Generates dummy data. Replace with a real implementation.
        return new Response(true);

        // Have AuthTokenDao remove auth token
    }

    public GetUserResponse getUser(GetUserRequest request) {
        // Validate request
        if (request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Request missing an alias");
        }

        // Have UserDao get user by their alias
        User userFound = daoFactory.getUserDao().getUser(request.getAlias());
        if (userFound == null) {
            return new GetUserResponse("No users exist with that alias");
        }

        // Return response
        return new GetUserResponse(userFound);
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        // Validate request
        if (request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Request missing a user");
        }

        int followingCount = daoFactory.getUserDao().getFollowingCount(request.getUser().getAlias());

        // Failure
        if (followingCount == -1) {
            throw new RuntimeException("[ServerError] Unable to get following count from database");
        }

        // Return response
        return new GetFollowingCountResponse(followingCount);
    }

    public GetFollowersCountResponse getFollowersCount(GetFollowersCountRequest request) {
        // Validate request
        if (request.getUser() == null) {
            throw new RuntimeException("[BadRequest] Request missing a user");
        }

        int followersCount = daoFactory.getUserDao().getFollowersCount(request.getUser().getAlias());

        // Failure
        if (followersCount == -1) {
            throw new RuntimeException("[ServerError] Unable to get following count from database");
        }

        // Return response
        return new GetFollowersCountResponse(followersCount);
    }








    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyAuthenticatedUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy user who matches the alias passed in.
     */
    User getDummyUserByAlias(String alias) {
        return getFakeData().findUserByAlias(alias);
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return new FakeData();
    }
}
