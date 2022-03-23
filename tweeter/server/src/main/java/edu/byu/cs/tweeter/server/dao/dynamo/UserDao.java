package edu.byu.cs.tweeter.server.dao.dynamo;

import edu.byu.cs.tweeter.model.domain.User;

public class UserDao implements IUserDao {

    @Override
    public User create(String firstName, String lastName, String alias, String imageUrl) {
        // FIXME -- Hard-coded
        return new User(firstName, lastName, alias, imageUrl);
    }

    @Override
    public User login(String username, String password) {
        return null;
    }

    @Override
    public User get(String alias) {
        return null;
    }
}
