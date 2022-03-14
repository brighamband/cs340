package edu.byu.cs.tweeter.model.net.response;

import edu.byu.cs.tweeter.model.domain.User;

public class GetUserResponse extends Response {

    private User user;

    /**
     * Unsuccessful response
     */
    public GetUserResponse(String message) {
        super(false, message);
    }

    /**
     * Successful response
     */
    public GetUserResponse(User user) {
        super(true, null);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
