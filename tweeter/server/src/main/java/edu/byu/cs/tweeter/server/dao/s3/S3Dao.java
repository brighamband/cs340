package edu.byu.cs.tweeter.server.dao.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class S3Dao implements IS3Dao {
    public String uploadImage(String username, String imageString) {
        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-2")
                .build();

        try {
            // Get name of S3 bucket
            String bucketName = s3.listBuckets().get(0).getName();

            // Set up keyName (USERNAME.png)
            String keyName = username + ".png";

            // Set up input stream
            byte[] array = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(array);

            // Set up metadata
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("image/png");
            metadata.setContentLength(array.length);

            // Perform put
            s3.putObject(bucketName, keyName, inputStream, metadata);

            // Grab the url of the stored image
            return s3.getUrl(bucketName, keyName).toString();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
