package org.compax.colombia.email;

import lombok.extern.log4j.Log4j2;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import java.io.File;
import java.io.IOException;

@Log4j2
public class EmailService {
    private final EmailSession emailSession;
    private final EmailMessage emailMessage;

    /**
     * Constructor for EmailService.
     *
     * @param host The SMTP(Protocolo simple de transferencia de correo) host address.
     * @param user The email address to send from.
     * @param password The password for the email account.
     */
    public EmailService(String host, String user, String password) {
        this.emailSession = new EmailSession(host, user, password);
        this.emailMessage = new EmailMessage(user);
    }

    /**
     * Sends an email with an attached file.
     *
     * @param recipient The recipient's email address.
     * @param file The file to attach to the email.
     * @throws MessagingException If there is an error during email creation or sending.
     * @throws IOException If the file cannot be attached.
     */
    public void sendEmailWithAttachment(String recipient, File file) throws MessagingException, IOException {
        log.info("Creating email session for sending email to: {}", recipient);
        Session session = emailSession.createSession();
        log.info("Email session created successfully.");

        log.info("Creating email message with attachment: {}", file.getName());
        Message message = emailMessage.createMessage(session, recipient, "Invoice sent", "Please find the attached invoice.", file);
        log.info("Email message created successfully.");

        log.info("Sending email to: {}", recipient);
        Transport.send(message);
        log.info("Email sent successfully to: {}", recipient);
    }
}
