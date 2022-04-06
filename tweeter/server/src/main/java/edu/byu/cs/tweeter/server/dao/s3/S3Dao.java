package edu.byu.cs.tweeter.server.dao.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class S3Dao implements IS3Dao {

    private static String DEFAULT_IMAGE_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png";
    private static String BUCKET_NAME = "tweeter-cs340-profile-pics";

    public String uploadImage(String username, String imageString) {
        if (imageString.equals("DEFAULT")) {
            return DEFAULT_IMAGE_URL;
        }

        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-2")
                .build();

        try {
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
            s3.putObject(BUCKET_NAME, keyName, inputStream, metadata);
            // System.out.println("Successfully put " + keyName + " in bucket");

            // Grab the url of the stored image
            return s3.getUrl(BUCKET_NAME, keyName).toString();

        } catch (Exception ex) {
            System.out.println("Threw exception..");
            System.out.println(ex.getMessage());
            return null;
        }
    }
}
