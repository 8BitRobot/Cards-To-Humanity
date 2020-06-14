package outreach;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import storage.DatabaseStorage;

import java.io.IOException;

public class EmailSender {
    private SendGrid sendGrid;

    public EmailSender(String SENDGRID_API_KEY, DatabaseStorage databaseStorage) {
        sendGrid = new SendGrid(SENDGRID_API_KEY);
    }

    public boolean scheduleEmail(String[] recipients, int[] card_ids, String subject, long sendingTime) {
        Mail mail = new Mail();
        Personalization personalization = new Personalization();
        for (int i = 0; i < recipients.length; i++) {
            personalization.addBcc(new Email(recipients[i]));
        }
        mail.addPersonalization(personalization);
        mail.setFrom(new Email("outgoingcards@cardstohumanity.org"));
        mail.setSubject(subject);
        Content content = new Content("text/plain", "HELLO WORLD!");
        mail.addContent(content);
        mail.setSendAt(sendingTime);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 200) {
                return true;
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
