package edu.byu.cs.tweeter.server.dao.s3;

public class S3Factory implements IS3Factory {
    @Override
    public S3Dao getS3Dao() {
        return new S3Dao();
    }
}
