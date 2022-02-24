Complete the exercise in Java and submit your work here.

In this exercise you will:

Create a web API for the Lambda function you wrote to send email in the Lambda/IAM exercise
Learn to pass inputs to your Lambda function through the HTTP request body
Learn to pass inputs to your Lambda function through the HTTP request URL and headers
Test your web API internally by calling it from within the AWS environment
Deploy your web API
Test your web API externally by calling it from outside the AWS environment using Curl or Postman
Generate a Swagger file with documentation describing your API.
Assumptions
You have already completed the Lambda/IAM exercise.

Steps
Login to the AWS API Gateway Console
https://us-west-2.console.aws.amazon.com/apigateway (Links to an external site.)

Create a new web API (in the same region that your Lambda from the previous exercise is running in)
Click the Create API button
On REST API (not private) click "Build"
Select New API
Specify API name and description
Select Regional for Endpoint Type
Click "Create API"
Next, we will add some resources to your web API. Resources define the URLs that clients will use when calling your web API operations.
Select “Resources” on the left side.

Add the /sendemail resource to your web API.
Select the root resource ( / )
In the Actions menu, select Create Resource
Uncheck the box "Configure as proxy resource" if it is checked
Fill in Resource Name: sendemail
Fill in Resource Path: sendemail
Check the box "Enable API Gateway CORS"
Click the "Create Resource" button

Add a POST method to the /sendemail resource. (This means that clients will use HTTP POST requests to call the /sendemail endpoint.)
Select the /sendemail resource
In the Actions menu, select Create Method
In the dropdown that appears below the resource, select POST as the new method type
Click the checkmark to create the method
For Integration type select "Lambda Function"
Uncheck the box "Use Lambda Proxy Integration"
For "Lambda Region" leave it as the default
For "Lambda Function" select the Lambda function you created in the Lambda/IAM exercise (e.g., send_email)
Check the box "Use Default Timeout"
Click "Save"
Click "OK" when asked if you want to give API Gateway permission to call your Lambda function
You have now created a web API endpoint for calling your send_email Lambda function.  This endpoint can be called by using an HTTP POST request that has /sendemail as the URL path and an appropriate JSON object in the HTTP request body (whatever JSON is expected by your send_email Lambda function).

Do an internal test of your /sendemail endpoint.
Click Test (lightning bolt)
In the "Request Body" field, enter a JSON object containing:
{
        "to": "INSERT A TO EMAIL ADDRESS HERE",
        "from": "INSERT A FROM EMAIL ADDRESS HERE",
        "subject": "Test Message",
        "textBody": "This is a test ...",
        "htmlBody": "This is a test ..."
}
Click the Test (lightning bolt) button
Look in the "Logs" field to see the log output for the test request.
If all went well, an email should have been sent.

Deploy your API.  This will make it callable from outside the AWS environment by anyone on the Web.
In the Actions menu, select Deploy API
In the "Deployment Stage" drop-down, select [New Stage]
Give your stage a name (e.g., dev) and description
Click the Deploy button
It should take you to your new stage in the "Stages" page. The URL for your deployed API should be at the top where it says "Invoke URL:"

Do an external test of your /sendemail endpoint.
Using Curl, Postman, or an equivalent tool, call your web API from outside the AWS environment.  Curl is a command-line program that lets you construct and send HTTP requests. Postman is a GUI-based tool that lets you do the same thing.  You may choose whichever you prefer: command-line or GUI
 If you want to use Curl, do the following:
Try running the "curl" command in a shell.  If you don't already have it installed, you can download it from https://curl.haxx.se/download.html (Links to an external site.)
Put the request body you want to send into a text file, e.g., data.txt
Run the following curl command to call your web API:
curl -d @data.txt -X POST <WEB-API-URL>
For example,

curl -d @data.txt -X POST https://gqv3z38u0i.execute-api.us-west-2.amazonaws.com/dev/sendemail (Links to an external site.)
If you want to use Postman, do the following:
If you don't have it, you can download Postman here:
https://www.getpostman.com/downloads/ (Links to an external site.)
In Postman, create a request, select POST as the request type, specify your web API's URL, and in the Body tab select "raw" and enter the request JSON object
Click the Send button

So far we have been specifying the email parameters in a JSON object contained in the HTTP request body.  Next, you will learn how to send parameters in the URL and HTTP headers instead of the request body.  Specifically, the "to" and "from" email addresses will be specified in the URL, and the subject and body text will be specified in HTTP headers named "EmailSubject" and "EmailText".  We will define the URL parameters and HTTP headers used to pass in the email parameters.

In the API Gateway console, underneath the /sendemail resource create a sub-resource with the following values:
Configure as proxy resource: No
Resource Name:  to
Resource Path:  {to}
Enable API Gateway CORS: Yes
The curly braces in the path mean that this part of the URL is variable, not fixed.

Underneath the /sendemail/{to} resource create a sub-resource with the following values:
Configure as proxy resource: No
Resource Name:  from
Resource Path:  {from}
Enable API Gateway CORS: Yes
Again, the curly braces in the path mean that this part of the URL is variable, not fixed.

Add a POST method to the /sendemail/{to}/{from} resource.  (This means that clients will use HTTP POST requests to call the /sendemail/{to}/{from} endpoint.)
Select the /sendemail/{to}/{from} resource
In the Actions menu, select Create Method
Select POST as the new method type
Click the checkmark to create the method
Integration type: Lambda Function
Use Lambda Proxy Integration: No
Lambda Region: accept default
Lambda Function: select the Lambda function you created in the Lambda/IAM exercise (e.g., send_email)
Use Default Timeout: Yes
Click "OK" when asked if you want to give API Gateway permission to call your Lambda function.

Next, we need to define the HTTP headers that callers should send to the /sendemail/{to}/{from} POST method. We will define a header named “EmailSubject” that will be used to pass in the email’s subject, and a header named “EmailText” that will be used to pass in the email’s body text.
Click the “Method Request” link.
Click on “HTTP Request Headers” to expand that section.
Click “Add Header” to create a header and name it “EmailSubject”. Click the check mark to save, then make it required by clicking the Required checkbox.
Click “Add Header” to create a header named “EmailText”, and make it required by clicking the Required checkbox.
This tells API Gateway that calls to the /sendemail/{to}/{from} POST method should contain these two HTTP headers that define the email’s subject and body text.
Click the "<-Method Execution" link

Next, we need to map the {to} and {from} URL parameters, as well as the "EmailSubject" and "EmailText" HTTP headers to a JSON object that will be sent to the Lambda function.  This will be accomplished by creating a “mapping template”.  Mapping templates describe how to map the parts of an HTTP request (URL parameters, HTTP headers, etc.) to a JSON object that will be passed to the Lambda function.
Click "Integration Request"
Keep the default values for all of the settings
Click on "Mapping Templates" to expand that section.
For "Request body passthrough", select: Never
Click "Add mapping template"
For the Content-Type, enter "application/json" (it looks like it's already there, but it isn't) and click the check mark.  This setting means that this mapping template will only be applied to HTTP requests that contain “application/json” in the HTTP Content-Type header.
Enter the following text for the "application/json" template:
{

 "to": "$input.params('to')",
 "from": "$input.params('from')",
 "subject": "$input.params('EmailSubject')",
 "textBody": "$input.params('EmailText')",
 "htmlBody": "$input.params('EmailText')"

}
Click the "Save" button
Go back to the Method configuration screen by clicking “<- Method Execution” in the top-left corner)

Do an internal test of your /sendemail/{to}/{from} endpoint.
Click Test (lightning bolt)
In the Path section, enter values for the {from} and {to} email addresses
In the Headers text area, enter the following text:
EmailSubject: My Email Subject
EmailText: My Email Text
Click the Test (lightning bolt) button
Look in the "Logs" field to see the log output for the test request.
If all went well, an email should have been sent.

Re-deploy your API
Select the stage you created earlier
Click the Deploy button
The URL should be the same as before if you used the same stage

Externally Test Your Web API
 Using Curl, Postman, or an equivalent tool, call your web API from outside the AWS environment
If you want to use Curl, do the following:
Run the following curl command to call your web API:
curl -X POST -H "EmailSubject: My Email Subject" -H "EmailText: My Email Text" <WEB-API-URL>/<TO-EMAIL-ADDRESS>/<FROM-EMAIL-ADDRESS>
For example,

curl -X POST -H "EmailSubject: My Email Subject" -H "EmailText: My Email Text" https://gqv3z38u0i.execute-api.us-west-2.amazonaws.com/dev/sendemail/bob@uvnets.com/bob@gmail.com (Links to an external site.)
If you want to use Postman, do the following:
In Postman, create a request, select POST as the request type, specify your web API's URL.
Include the TO and FROM emails addresses in the URL.
In the Headers tab, create the following HTTP headers
KEY: EmailSubject VALUE: My Email Subject

KEY: EmailText  VALUE: My Email Text
Click the Send button
(OPTIONAL. This step tells you how you can generate and use a client SDK in your project. This is NOT REQUIRED for the exercise.) To make it easier to call your API from a client program, API Gateway can generate a client SDK that hides some of the details involved in calling your API. Using a client SDK in your project is optional. The alternative is to construct and send HTTP requests on your own. If you want to use a client SDK, you need to generate one for your environment of choice, and incorporate it into your client project.  Client SDKs for the following environments can be generated: Java, Android, iOS, Javascript, Ruby

Add documentation to your API methods.
For each method in your API:
In the Resources tab, select the method.
In the upper right-hand corner of the console, click the grey book icon.
Edit the description
Click "Save", then "Close"
Go to the Documentation tab on the left-hand panel.
All of your descriptions should be displayed.
In the upper-right-hand corner, click "Publish Documentation"
Select the Stage of the API that you created earlier.
Input any version number for your documentation.
Click "Publish"

Export your API as a Swagger file
Click "Stages" on the left side panel
Select the stage you created earlier
Click "Export"
Click "Export as Swagger"
Select either JSON or YAML
The file should be generated and displayed in your browser.
Submission
Submit your generated Swagger file through Canvas.