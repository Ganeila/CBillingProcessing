package org.compax.colombia.email;

import lombok.extern.log4j.Log4j2;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Log4j2
public class EmailSession {
    private final String host;
    private final String user;
    private final String password;

    /**
     * Constructor for EmailSessionFactory.
     *
     * @param host The SMTP(Protocolo simple de transferencia de correo) host address.
     * @param user The email address to authenticate.
     * @param password The password for the email account.
     */
    public EmailSession(String host, String user, String password) {
        this.host = host;
        this.user = user;
        this.password = password;
    }

    /**
     * Creates and returns a new email session.
     *
     * @return The configured email session.
     */
    public Session createSession() {
        //TODO: update values here
        log.info("Configuring email session with host: {}", host);
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        return Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });
    }
}
