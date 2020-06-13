package outreach;

import com.sendgrid.SendGrid;

public class EmailSender {
    private SendGrid sendGrid;

    public EmailSender(String SENDGRID_API_KEY) {
        sendGrid = new SendGrid(SENDGRID_API_KEY);
    }
}
