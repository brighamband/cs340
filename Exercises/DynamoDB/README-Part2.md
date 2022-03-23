Complete the exercise in Java and submit your work here.

In this exercise you will write a program that uses the AWS SDK to:

Query the “follows” table with and without pagination
Query the “follows_index” index with and without pagination
Assumptions
You have already configured your AWS credentials on your computer. This should have been done when you installed the AWS CLI on your computer.

Intellij is installed on your computer.

You have completed DynamoDB Exercise Part 1

Steps
Write Java code to query the “follows” table and the “follows_index” index. The example on this page demonstrates how to query a table: https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.04.html (Links to an external site.).
“Query” the “follows” table to return all of the users being followed by a user, sorted by “followee_handle”
Hint: Call QuerySpec.withScanIndexForward(true) to sort the result in ascending order
“Query” the “follows_index” index to return all of the users following a user, reverse sorted by “follower_handle”
Hint: Call Table.getIndex to get a reference to the Index object so the index can be queried
Hint: Call QuerySpec.withScanIndexForward(false) to sort the result in descending order
Make copies of the previous two queries, and modify them to return the query results in pages of size ten (i.e., ten at a time)
This means you will need to re-execute the query multiple times in order to fetch all of the results.
Hint: Call withMaxResultSize() (not withMaxPageSize()) on the QuerySpec to tell it how many items to return in each page
Hint: Call withExclusiveStartKey() on the QuerySpec to tell DynamoDB the primary key value of the last item returned in the previous page (this is not necessary for the first page). This is how DynamoDB knows where the next page should start.
Hint: Here’s how you get the primary key value for the last item returned in the current page:
On the ItemCollection<QueryOutcome> object returned by the query, call getLastLowLevelResult() to get a reference to the QueryOutcome.
Call getQueryResult() on the QueryOutcome to get a reference to the QueryResult.
Call getLastEvaluatedKey() on the QueryResult to get the last item’s primary key value. If this value is non-null, there is more data to retrieve.
Hint: The parameter you pass to withExclusiveStartKey() is an instance of PrimaryKey, which you create from the values in the map returned by getLastEvaluatedKey()
Hint: The values in the last evaluated key map are of type AttributeValue. Convert them to strings to pass to your PrimaryKey constructor by calling getS().
Submission
Submit your code as individual Java classes. DO NOT SUBMIT A .zip FILE.

More Information
The full AWS SDK documentation can be found here in the SDKs section:https://aws.amazon.com/tools/
