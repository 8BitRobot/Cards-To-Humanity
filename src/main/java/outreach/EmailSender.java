package outreach;

import com.sendgrid.*;
import storage.DatabaseStorage;

import java.io.IOException;

public class EmailSender {
    private SendGrid sendGrid;
    private DatabaseStorage databaseStorage;

    private class BackgroundThread implements Runnable {
        private boolean stopFlag = false;

        public synchronized void stop() {
            stopFlag = true;
        }

        // Every minute, this thread checks for any unsent emails with scheduled sending times greater than or equal to the current time. If any are found, they are sent.
        @Override
        public void run() {
            while (!stopFlag) {
                System.out.println("SENDING EMAIL...");
                try {
                    Thread.sleep(60L * 1000L);
                    //PendingEmail[] pendingEmails = databaseStorage.getPendingEmails();
                }
                catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public EmailSender(String SENDGRID_API_KEY, DatabaseStorage databaseStorage) {
        this.sendGrid = new SendGrid(SENDGRID_API_KEY);
        this.databaseStorage = databaseStorage;
        BackgroundThread backgroundThread = new BackgroundThread();
        Thread backgroundThreadThread = new Thread(backgroundThread);
        backgroundThreadThread.start();
    }

    public boolean scheduleEmail(String[] recipients, int[] card_ids, String subject, long sendingTime) {
        Mail mail = new Mail();
        Personalization personalization = new Personalization();
        for (int i = 0; i < recipients.length; i++) {
            personalization.addBcc(new Email(recipients[i]));
        }
        personalization.addTo(new Email("empty@cardstohumanity.com"));
        mail.addPersonalization(personalization);
        mail.setFrom(new Email("outgoingcards@cardstohumanity.com", "Cards To Humanity"));
        mail.setSubject(subject);
        Content content = new Content("text/plain", "HELLO WORLD again!");
        mail.addContent(content);
        mail.setSendAt(sendingTime);

        Request request = new Request();
        try {
            request.method = Method.POST;
            request.endpoint = "mail/send";
            request.body = mail.build();
            Response response = sendGrid.api(request);
            System.out.println(response);
            System.out.println(response.body);
            System.out.println(response.statusCode);
            if (response.statusCode == 200 || response.statusCode == 202) {
                return true;
            }
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
        return false;
    }
}
