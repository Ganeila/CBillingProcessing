package org.compax.colombia.billing;

import lombok.extern.log4j.Log4j2;
import org.compax.colombia.email.EmailService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Log4j2
public class BillingProcessor {
    private static final Map<String, String> COUNTRY_EMAILS = Map.of(
            //TODO: update mails here
            "Colombia", "colombia@compax.at",
            "Mexico", "mexico@compax.at",
            "Chile", "chile@compax.at",
            "Peru", "peru@compax.at"
    );

    private final EmailService emailService;

    public BillingProcessor(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Processes all country folders to find and send invoices.
     */
    public void processAllCountries() {
        log.info("Starting to process all country folders...");
        COUNTRY_EMAILS.keySet().forEach(this::processCountryFolder);
        log.info("Finished processing all country folders.");
    }

    /**
     * Processes a specific country's folder, sending invoices found within.
     *
     * @param country The name of the country whose folder to process.
     */
    private void processCountryFolder(String country) {
        log.info("Processing folder for country: {}", country);
        String folderPath = "./" + country;
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".pdf"));

        if (files == null || files.length == 0) {
            log.info("No invoices found in folder: {}", folderPath);
            return;
        }

        // Get the current year and month to create the subfolder "enviados/yyyyMM"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
        String currentMonthYear = dateFormat.format(new Date());
        File sentFolder = new File(folder, "enviados/" + currentMonthYear);

        // Create the "enviados/yyyyMM" folder if it doesn't exist
        if (!sentFolder.exists()) {
            boolean created = sentFolder.mkdirs(); // Create directories if necessary
            if (created) {
                log.info("Created sent folder for country: {} - {}", country, sentFolder.getAbsolutePath());
            } else {
                log.warn("Could not create sent folder for country: {} - {}", country, sentFolder.getAbsolutePath());
            }
        }

        // Process each file(pdf) found in the country folder
        for (File file : files) {
            try {
                log.info("Sending file: {} from folder: {}", file.getName(), folderPath);
                emailService.sendEmailWithAttachment(COUNTRY_EMAILS.get(country), file);
                moveFileToSentFolder(file, sentFolder.getAbsolutePath());

                log.info("File successfully sent and moved: {}", file.getName());

            } catch (Exception e) {
                log.error("Error processing file: {}", file.getName(), e);
            }
        }
    }

    /**
     * Moves the file to the "enviados/yyyyMM" folder.
     *
     * @param file        The file to be moved.
     * @param sentFolderPath The path of the sent folder.
     */
    private void moveFileToSentFolder(File file, String sentFolderPath) {
        File destination = new File(sentFolderPath, file.getName());
        boolean renamed = file.renameTo(destination);
        if (renamed) {
            log.info("File moved to: {}", destination.getAbsolutePath());
        } else {
            log.error("Failed to move file to: {}", destination.getAbsolutePath());
        }
    }
}
