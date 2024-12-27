package org.compax.colombia;

import lombok.extern.log4j.Log4j2;
import org.compax.colombia.billing.BillingProcessor;
import org.compax.colombia.email.EmailService;

@Log4j2
public class BillingProcessorApp {

    /**
     * The application will perform the scan once per day and will log the progress
     * both to the console and to the factura_envio.log file as per the Log4j2 configuration.
     */
    public static void main(String[] args) {
        //TODO: update values here
        EmailService emailService = new EmailService("smtp.example.com", "your-email@example.com", "your-email-password");
        BillingProcessor processor = new BillingProcessor(emailService);
        System.setProperty("log4j.configurationFile", "log4j2.xml"); // Log4j2 configuration

        log.info("Application started. Monitoring country folders for invoices...");

        boolean running = true;
        while (running) {
            processor.processAllCountries();
            try {
                // Sleep for 24 hours (86400000 ms) before next scan
                log.info("Sleeping for 24 hours before next scan...");
                Thread.sleep(86400000); // Sleep for 24 hours (1 day)
            } catch (InterruptedException e) {

                log.error("Error during wait cycle, thread interrupted", e);
                Thread.currentThread().interrupt();
                running = false;
            }
        }

        log.info("Application has stopped.");
    }
}