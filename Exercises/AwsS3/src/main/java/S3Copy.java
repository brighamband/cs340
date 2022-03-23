import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.File;

public class S3Copy {

    public static void main(String[] args) {

        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-2")
                .build();

        try {
            // 1. get name of file to be copied from the command line
            File localFile = new File("file-to-upload.txt");

            // 2. get name of S3 bucket from the command line
            String bucketName = s3.listBuckets().get(0).getName();

            // 3. upload file to the specified S3 bucket using the file name as the S3 key
            s3.putObject(bucketName, localFile.getName(), localFile);

            System.out.println("Successfully copied " + localFile.getName() + " to S3");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}