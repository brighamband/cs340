package edu.byu.cs.tweeter.server.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowersCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
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

public class UserService extends Service {
  IS3Factory s3Factory;

  public UserService(IDaoFactory daoFactory) {
    super(daoFactory);
  }

  public UserService(IDaoFactory daoFactory, IS3Factory s3Factory) {
    super(daoFactory);
    this.s3Factory = s3Factory;
  }

  public LoginResponse login(LoginRequest request) {
    // Validate request
    if (request.getUsername() == null) {
      throw new RuntimeException("[BadRequest] Request missing a username");
    } else if (request.getPassword() == null) {
      throw new RuntimeException("[BadRequest] Request missing a password");
    }

    // Have UserDao check if User exists with that username
    User existingUser = daoFactory.getUserDao().getUser(request.getUsername());
    if (existingUser == null) {   // Handle failure case #1 - missing user
      return new LoginResponse("No users exist with that username");
    }

    // Check password
    String dbHashedPassword = daoFactory.getUserDao().getHashedPassword(request.getUsername());
    String reqHashedPassword = hashPassword(request.getPassword());

    // Handle failure case #2 - bad password
    if (!reqHashedPassword.equals(dbHashedPassword)) { // If passwords don't match
      return new LoginResponse("Invalid password for " + request.getUsername());
    }

    // Have AuthTokenDao create an auth token for the user
    AuthToken updatedAuthToken = daoFactory.getAuthTokenDao().create(request.getUsername());

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
    } else if (request.getImage() == null) {
      throw new RuntimeException("[BadRequest] Request missing an image");
    }

    // Have UserDao check to see if a user already exists with that username
    User existingUser = daoFactory.getUserDao().getUser(request.getUsername());
    if (existingUser != null) {
      return new RegisterResponse("A user already exists with that username");
    }

    System.out.println("Starting image upload");

    // Hash password
    String hashedPassword = hashPassword(request.getPassword());

    // Have S3Dao upload image to S3
     String imageUrl = s3Factory.getS3Dao().uploadImage(request.getUsername(), request.getImage());
//    String imageUrl = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";

    System.out.println("About to create in UserDao");

    // Have UserDao to create (register) a new user
    User newUser = daoFactory.getUserDao().create(
        request.getFirstName(), request.getLastName(),
        request.getUsername(), hashedPassword, imageUrl);
    // Handle failure
    if (newUser == null) {
      return new RegisterResponse("Failed to register new user");
    }

    // Get an auth token from the AuthTokenDao
    AuthToken newAuthToken = daoFactory.getAuthTokenDao().create(newUser.getAlias());

    // Return RegisterResponse
    return new RegisterResponse(newUser, newAuthToken);
  }

  public Response logout(LogoutRequest request) {
    if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Have AuthTokenDao remove auth token
    String tokenToRemove = request.getAuthToken().getToken();
    daoFactory.getAuthTokenDao().remove(tokenToRemove);

    // Return response
    return new Response(true);
  }

  public GetUserResponse getUser(GetUserRequest request) {
    // Validate request
    if (request.getAlias() == null) {
      throw new RuntimeException("[BadRequest] Request missing an alias");
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      throw new RuntimeException("[BadRequest] Auth token has expired");
    }

    // Have UserDao get user by their alias
    User userFound = daoFactory.getUserDao().getUser(request.getAlias());
    // Handle failure
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
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      throw new RuntimeException("[BadRequest] Auth token has expired");
    }

    // Get following count
    int followingCount = daoFactory.getUserDao().getFollowingCount(request.getUser().getAlias());

    // Handle failure
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
    } else if (request.getAuthToken() == null) {
      throw new RuntimeException("[BadRequest] Request missing an auth token");
    }

    // Validate auth token
    boolean isValidAuthToken = validateAuthToken(request.getAuthToken().getToken());
    if (!isValidAuthToken) {
      throw new RuntimeException("[BadRequest] Auth token has expired");
    }

    // Get followers count
    int followersCount = daoFactory.getUserDao().getFollowersCount(request.getUser().getAlias());

    // Handle failure
    if (followersCount == -1) {
      throw new RuntimeException("[ServerError] Unable to get following count from database");
    }

    // Return response
    return new GetFollowersCountResponse(followersCount);
  }

  private static String hashPassword(String passwordToHash) {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(passwordToHash.getBytes());
      byte[] bytes = md.digest();
      StringBuilder sb = new StringBuilder();
      for (byte aByte : bytes) {
        sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "FAILED TO HASH";
  }
}
