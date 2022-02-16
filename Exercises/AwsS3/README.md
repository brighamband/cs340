Complete the exercise in Java.

In this exercise you will write a Java program that uses the AWS SDK to upload a local file into an S3 bucket.

Assumptions
You have already configured your AWS credentials on your computer.  This should have been done when you installed the AWS CLI on your computer.

Intellij is installed on your computer.

 

Steps
Create a directory for this exercise
Create a new Intellij project and select Maven on the left hand side.
Maven
Do not select an archetype and click "Next".
Name your project and select where it will save to.
Click the drop down labeled "Artifact Coordinates". Set the group id to whatever you want your package structure to be. For example "edu.byu.cs340" will tell Maven where your main package is.
Select "Finish".
In the new project, add dependencies on the AWS SDK Java library
Go to https://mvnrepository.com  (Links to an external site.)and search for the following:
AWS SDK For Java Core - This will add a dependency on the core AWS Java SDK library.
AWS Java SDK For Amazon S3 - This will add a dependency on the S3 portion of the AWS Java SDK library.
JAXB API (Optional) - This will add a dependency that the AWS SDK uses to convert objects to XML. This library was included in Java 6, 7, and 8, but not in later versions. If you are using Java 9 or higher and don't include it, Intellij will give you a warning and automatically fallback to an older version of Java. In other words, if you don't want to include it, you can just ignore the warning and your code will still work.
Find the latest version of each dependency (At the time of this writing, the latest version is 1.11.720)
Copy the dependency for Maven.
Maven
Open pom.xml and add the following below the version tag:
<properties>
   <maven.compiler.source>1.11</maven.compiler.source>
   <maven.compiler.target>1.11</maven.compiler.target>
</properties>
Create a dependencies tag:
<dependencies></dependencies>
In between the dependencies tag paste what you copied for each of the dependencies.
On the right hand side, click the Maven tab, and then click the refresh button. Maven will now download and import the dependencies and everything else it needs. NOTE: This will take some time, depending on your internet connection it can take 5-10 minutes.
Create a Java file containing the following code (you might need to change the AWS region value if you’re using a region other than us-west-2):
import com.amazonaws.services.s3.AmazonS3;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;

 

public class S3Copy {

   public static void main(String[] args) {
  
        // Create AmazonS3 object for doing S3 operations
        AmazonS3 s3 = AmazonS3ClientBuilder
                           .standard()
                           .withRegion("us-west-2")
                           .build();
 
       // Write code to do the following:
       // 1. get name of file to be copied from the command line
       // 2. get name of S3 bucket from the command line
       // 3. upload file to the specified S3 bucket using the file name as the S3 key
 }
}
 

***Note: When accessing an s3 bucket via the command line, the proper syntax is “s3://<name-of-bucket>”. When accessing your bucket programmatically, only the name of your bucket is required (i.e. without the “s3://” prefix).

 

The online documentation for the AWS Java SDK can be found here:
https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html (Links to an external site.)
Look at the documentation for the com.amazonaws.services.s3.AmazonS3 class
https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-s3.html (Links to an external site.)
Test your program by using it to upload some local files into an S3 bucket in your AWS account.
Submission

Submit the single .java file containing the code to upload to S3. DO NOT SUBMIT A .zip FILE.
Submit your Java code here.
More Information
The full AWS SDK documentation can be found here in the SDKs section: https://aws.amazon.com/tools/