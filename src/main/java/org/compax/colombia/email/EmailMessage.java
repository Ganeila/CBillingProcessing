package org.compax.colombia.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;

public class EmailMessage {
    private final String sender;

    public EmailMessage(String sender) {
        this.sender = sender;
    }

    /**
     * Creates and returns a new email message with an attachment.
     *
     * @param session The email session to use.
     * @param recipient The recipient's email address.
     * @param subject The subject of the email.
     * @param body The body text of the email.
     * @param file The file to attach to the email.
     * @return The configured email message.
     * @throws MessagingException If there is an error creating the message.
     * @throws IOException If the file cannot be attached.
     */
    public Message createMessage(Session session, String recipient, String subject, String body, File file) throws MessagingException, IOException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject(subject);

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(body);

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(file);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);
        return message;
    }
}
