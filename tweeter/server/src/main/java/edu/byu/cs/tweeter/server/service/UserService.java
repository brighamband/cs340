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
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {

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
        if (request.getUsername() == null) {
            throw new RuntimeException("[BadRequest] Request missing a username");
        } else if (request.getPassword() == null) {
            throw new RuntimeException("[BadRequest] Request missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.
        User user = getDummyAuthenticatedUser();
        AuthToken authToken = getDummyAuthToken();
        return new RegisterResponse(user, authToken);
    }

    public Response logout() {
        // TODO: Generates dummy data. Replace with a real implementation.
        return new Response(true);
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
