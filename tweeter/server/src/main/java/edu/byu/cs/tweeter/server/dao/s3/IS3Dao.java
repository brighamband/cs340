package edu.byu.cs.tweeter.server.dao.s3;

public interface IS3Dao {
    String uploadImage(String username, String imageString);
}
