In this exercise you will:

Create an SQS queue
Write a Java program that sends messages to your queue
Write a Java Lambda function that processes messages sent to your queue


Assumptions
You have already configured your AWS credentials on your computer.  This should have been done when you installed the AWS CLI on your computer.

Intellij is installed on your computer.



Steps
Step 1:  Queue Creation:
Go the the AWS Simple Queue Service (SQS) web console
Create a new SQS queue (NOTE: The SQS queue you created when doing the pre-class preparation was a FIFO queue. For this exercise we are using a Standard queue. Since you cannot change the type of a queue after it is created, you need to create a new queue for this exercise.)
Click the “Create Queue” button
Select “Standard” type (not "FIFO")
Enter a name for your queue
Create with default settings (you can also specify other accounts, IAM users, and roles are allowed to interact with the queue)
Step 2:  Send a message to your queue through the SQS web console
Click on your new queue
On the queue page, click the "Send and receive messages" button
Send a message
Provide some text for the message body
Click the “Send Message” button
Verify that the message was sent
Click “Poll for Messages”
You should see the message you sent in the queue (click More Details to see more details)
Step 3:  Write a Java program that sends messages to your queue.
Create a directory for this exercise
Create a new Intellij project
When creating, select the latest version of Java supported by AWS Lambda
At the time of writing this, it is Java 11
Add dependencies on the AWS SDK Java library:
How to add dependencies:
Select the File -> Project Structure menu
Select “Modules” under “Project Structure”
Select the “Dependencies” tab
Click the “plus” icon to add a new dependency
Select “Library”
Select “From Maven”
Enter the Maven coordinates
These are the coordinates for the dependencies you will need for this part
com.amazonaws:aws-java-sdk-core:1.11.977 -- This will add a dependency on the core AWS Java SDK library.
com.amazonaws:aws-java-sdk-sqs:1.11.977 -- This will add a dependency on the SQS portion of the AWS Java SDK library.
Create a class named SqsClient containing the following code.  The queue URL can be found in the SQS console.
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;


public class SqsClient {
public static void main(String[] args) {

       String messageBody = "*** PUT YOUR MESSAGE BODY HERE ***";        
       String queueUrl = "*** PUT YOUR QUEUE URL HERE ***";
                
       SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody)
                .withDelaySeconds(5);
       
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
       SendMessageResult send_msg_result = sqs.sendMessage(send_msg_request);
       
       String msgId = send_msg_result.getMessageId();        
       System.out.println("Message ID: " + msgId);
}
}
More information on accessing SQS from Java can be found here:
https://docs.aws.amazon.com/sdk-for-java/v1/developer-guide/examples-sqs-messages.html (Links to an external site.)
Test your program
Run your program
Go to your SQS console and verify that the messages sent by your program are showing up in the queue.
Step 4: Write a Java Lambda function that processes messages sent to your queue.
In the IntelliJ project you created earlier, add dependencies on the AWS Lambda Java library
com.amazonaws:aws-lambda-java-core:1.2.1 -- This will add a dependency on the Lambda portion of the AWS Java SDK library.
com.amazonaws:aws-lambda-java-events:3.7.0 -- This library contains the SQSEvent class, which will be used by your Lambda function.
Write the code for your lambda function.
In your project, create a Java package named “sqs”.
In the “sqs” package, create a class named QueueProcessor that will contain the “handler” for your Lambda function. Add the following code to this file.
package sqs;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public class QueueProcessor implements RequestHandler<SQSEvent, Void> {

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        for (SQSEvent.SQSMessage msg : event.getRecords()) {
            // TODO:
            // Add code to print message body to the log
        }
        return null;
    }
}
Fill in the code that logs the message body. Note: You can log to an Amazon Cloudwatch log simply by doing a System.out.println(...)
Once your program compiles, add an “artifact” to your Intellij project that creates a JAR file containing your compiled code.
Select the File -> Project Structure menu
Select “Artifacts” on the left side of the “Project Structure” dialog
To add a new artifact, click the “plus” button at the top of the dialog
Select JAR -> From modules with dependencies
In the “Create JAR from Modules” dialog, keep the default options, and click OK
Check the “Include in project build” check box
Rebuild the project, which will result in a JAR file being created in the “out/artifacts/<DIR>/ folder. This JAR file can be used to deploy your Lambda function to AWS
Your Lambda function needs to run with an IAM role that has SQS permissions.  Modify the IAM role you created in the “Lambda / IAM” exercise to allow SQS access:
Go to the IAM console
On the left, select “Roles”
Click on the role you created previously for the “Lambda / IAM” exercise (e.g., cs340lambda)
Click the “Attach Policies” button
On the "Attach Permissions" screen, attach this policy to your role: AmazonSQSFullAccess.  This will allow your Lambda function to access your SQS queues.
Step 5:  Deploy your Lambda function.
Create the function
Go to your AWS Lambda console
Select “Functions” on the left side of the Lambda screen
Click “Create Function” button
Give your function a name (e.g., queue_processor)
As your function’s Runtime select “Java 11”
Under Permissions, expand "Change default execution role"
Select “Use an existing role” and select the IAM role you configured earlier (e.g., cs340lambda)
Click the “Create function” button.
Upload your code to the function
Click on your function
On the tab configuration screen
In the “Function code” section, select the "Actions" dropdown and click on "Upload a .zip file"
Upload the JAR file you created earlier
In the “Runtime settings” field, provide the name of your Lambda function handler (e.g., sqs.QueueProcessor::handleRequest)
Step 6: Connect your Lambda function to your SQS queue
Add a lambda trigger to your queue
Go to your queue on the SQS web console
Select the "Lambda triggers" tab and click the “Configure Lambda Function trigger” button
Select the name of the Lambda function you previously created (e.g., queue_processor)
Click the “Save” button
Now, whenever a message is sent to your SQS queue, the message will be sent to your Lambda function for processing. Verify that this is happening by running your Java program that sends messages to your queue, or send messages manually through the SQS console.
Test the complete queue
Run your SqsClient again and confirm that the message body appears in the Cloudwatch log for your Lambda function.
Note: You can find the CloudWatch log by selecting the 'CloudWatch' service from the AWS console, selecting 'Logs -> Log Groups' and then selecting the log group for your Lambda function.
Click a log stream from within the log group to find the log message containing your message body.
Step 7: Disconnect your Lambda trigger from your SQS queue
IMPORTANT: After completing the exercise, remove the lambda trigger from your queue, because SQS constantly polls the queue to detect new messages so it can call the lambda appropriately.  All of this polling adds to your AWS charges, so you will want to remove the lambda trigger to avoid this.


Submission
Submit a screenshot of the AWS Lambda Logs where it prints out the records that are read from the queue.

More Information
The full AWS SDK documentation can be found here in the SDKs section:https://aws.amazon.com/tools/