Complete the exercise in Java and submit your work here.

In this exercise you will:

Create a DynamoDB table named “follows” that keeps track of which users are following each other (as in Twitter).
Create a secondary index named “follows_index” on the “follows” table.
Write a program that uses the AWS SDK to insert, update, and delete items in the “follows” table
Assumptions
You have already configured your AWS credentials on your computer. This should have been done when you installed the AWS CLI on your computer.

Intellij is installed on your computer.

Steps
0 A. IMPORTANT: Update your IAM user to have DynamoDBFullAccess permissions before beginning this exercise

0 B. IMPORTANT: This exercise does not work with Java 16 (JVM 16). Downgrade to a supported version (such as 8, or 11) that we have tested this exercise to work with.

Go the the AWS DynamoDB web console
Create a table named “follows”.
Partition Key: a string named “follower_handle”. This attribute stores the Twitter handle of the user who is following another user.
Sort Key: a string named “followee_handle”. This attribute stores the Twitter handle of the user who is being followed.
This Primary Key lets you query the table by “follower_handle” and sort the results by “followee_handle”.
Select the “Indexes” tab and create an index named “follows_index”.
Partition Key: a string named “followee_handle. This is the same “followee_handle” attribute you created in the previous step.
Check “Add sort key”
Sort Key: a string named “follower_handle”. This is the same “follower_handle” attribute you created in the previous step.
This Primary Key lets you query the index by “followee_handle” and sort the results by “follower_handle”.
For “Projected attributes”, keep the “All” setting.
Next, you will write a Java program that performs operations on the “follows” table.
Create a directory for this exercise
Create a new Intellij project
In the new project, add dependencies on the AWS SDK Java library
Select the File -> Project Structure menu
Select “Modules” on the left side of the “Project Structure” dialog
Select the “Dependencies” tab
Click the “plus” icon in the top-right
Select “Library”
Select “From Maven”
Enter “com.amazonaws:aws-java-sdk-core:1.11.547”, and click OK. This will add a dependency on the core AWS Java SDK library.
Using the same process, add a Maven dependency on “com.amazonaws:aws-java-sdk-dynamodb:1.11.547”. This will add a dependency on the DynamoDB portion of the AWS Java SDK library.
Write Java code to put, get, update, and delete items in the “follows” table. The examples on this page demonstrate how to put, get, update, and delete items in a DynamoDB table: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.03.html (Links to an external site.). Also, the complete online documentation for the AWS Java SDK can be found here: https://docs.aws.amazon.com/AWSJavaSDK/latest/javadoc/index.html (Links to an external site.)

(Links to an external site.)Note: The GettingStarted document linked above shows examples of using the AmazonDynamoDBClientBuilder to create an instance of the AmazonDynamoDB class. You will need an instance of this class, but the example shows how to create a connection to localhost and you need to connect to the AWS region that has your DynamoDB table instead. This is done by replacing the call to 'withEndpointConfiguration' with a call to 'withRegion' instead. The call to 'withRegion' takes a single String parameter that specifies the name of the region that contains your DynamoDB table(s).

“Put” 25 items into the “follows” table all with the same follower:
“follower_handle”: string handle of the user who is following (e.g., “@FredFlintstone”)
This attribute should be the same across all of these 25 items
“follower_name”: string name of the user who is following (e.g., “Fred Flintstone”)
This attribute should be the same across all of these 25 items
“followee_handle”: string handle of the user being followed (e.g., “@ClintEastwood”)
This attribute should be different for each of these 25 items
“followee_name”: string name of the user being followed (e.g., “Clint Eastwood”)
This attribute should be different for each of these 25 items
"Put" 25 more items into the "follows" table, this time all with the same followee:
“follower_handle”: string handle of the user who is following (e.g., “@FredFlintstone”)
This attribute should be different for each of these 25 items
“follower_name”: string name of the user who is following (e.g., “Fred Flintstone”)
This attribute should be different for each of these 25 items
“followee_handle”: string handle of the user being followed (e.g., “@ClintEastwood”)
This attribute should be the same across all of these 25 items
“followee_name”: string name of the user being followed (e.g., “Clint Eastwood”)
This attribute should be the same across all of these 25 items

“Get” one of the items from the “follows” table using its primary key

“Update” the “follower_name” and “followee_name” attributes of one of the items in the “follows” table

“Delete” one of the items in the “follows” table using its primary key
Submission
Submit your code as individual Java classes. DO NOT SUBMIT A .zip FILE.

More Information
The full AWS SDK documentation can be found here in the SDKs section:https://aws.amazon.com/tools/ (Links to an external site.)
