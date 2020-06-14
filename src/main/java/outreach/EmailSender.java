package outreach;

import com.sendgrid.*;
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
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sendGrid.api(request);
            if (response.statusCode == 200) {
                return true;
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
