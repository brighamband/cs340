package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.RegisterResponse;
import edu.byu.cs.tweeter.model.net.response.Response;
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
        if (request.getUsername() == null){
            throw new RuntimeException("[BadRequest] Request missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Request missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = getDummyAuthenticatedUser();
        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        // Validate request
        if (request.getUsername() == null) {
            throw new RuntimeException("[BadRequest] Request missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Request missing a password");
        }

        // Have UserDao check to see if a user already exists with that username

        // Have S3Dao upload image to S3
        // FIXME -- HARD-CODED FOR NOW
        String imageUrl = s3Factory.getS3Dao().uploadImage(request.getUsername(), request.getImage());
        // Have UserDao to create (register) a new user
        User newUser = daoFactory.getUserDao().create(
                request.getFirstName(), request.getLastName(),
                request.getUsername(), imageUrl);

        // Get an auth token from the AuthTokenDao
        AuthToken newAuthToken = daoFactory.getAuthTokenDao()
                .create(newUser.getAlias(), request.getPassword());
        // Return RegisterResponse
        return new RegisterResponse(newUser, newAuthToken);

        // TODO: Generates dummy data. Replace with a real implementation.
//        User user = getDummyAuthenticatedUser();
//        AuthToken authToken = getDummyAuthToken();
//        return new RegisterResponse(user, authToken);

        // FIXME - IS CACHING NEEDED?
    }

    public Response logout() {
        // TODO: Generates dummy data. Replace with a real implementation.
        return new Response(true);

        // Have AuthTokenDao remove auth token
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if (request.getAlias() == null) {
            throw new RuntimeException("[BadRequest] Request missing an alias");
        }

        String alias = request.getAlias();
        // TODO: Generates dummy data. Replace with a real implementation.
        return new GetUserResponse(getDummyUserByAlias(alias));
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
