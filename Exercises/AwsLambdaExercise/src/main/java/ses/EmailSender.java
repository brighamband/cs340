package ses;

// these are the imports for SDK v1
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.simpleemail.*;
import com.amazonaws.services.simpleemail.model.*;
import com.amazonaws.regions.Regions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailSender {
    public EmailResult handleRequest(EmailRequest request, Context context) {

        LambdaLogger logger = context.getLogger();
        logger.log("Entering send_email");

        SendEmailResult sesResult = new SendEmailResult();

        try {
            AmazonSimpleEmailService client =
                    AmazonSimpleEmailServiceClientBuilder.standard()
                            .withRegion(Regions.US_EAST_2).build();

            // Use the AmazonSimpleEmailService object to send an email message
            // using the values in the EmailRequest parameter object
            SendEmailRequest sesRequest = new SendEmailRequest();
            // Set source
            sesRequest.setSource(request.from);
            // Set destination
            List<String> destList = new ArrayList<>();
            destList.add(request.to);
            sesRequest.setDestination(new Destination(destList));
            // Set message subject, bodyText, and bodyHtml
            Body body = new Body();
            body.setText(new Content(request.textBody));
            body.setHtml(new Content(request.htmlBody));
            Message message = new Message(new Content(request.subject), body);
            sesRequest.setMessage(message);

            sesResult = client.sendEmail(sesRequest);

            logger.log("Email sent!");
        } catch (Exception ex) {
            logger.log("The email was not sent. Error message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        finally {
            logger.log("Leaving send_email");
        }

        // Return EmailResult
        EmailResult result = new EmailResult();
        result.message = sesResult.getMessageId();
        result.timestamp = new Timestamp(new Date().getTime()).toString();
        return result;
    }
}
