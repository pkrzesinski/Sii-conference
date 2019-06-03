package com.project.siiproject.feature.emailsender;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EmailSender {

    private static final Logger LOG = LogManager.getLogger(EmailSender.class);
    private static final String EMAIL_CONTAINER = "emails.txt";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    private String topic;
    private String emailAddress;
    private String message;
    private LocalDateTime sendTime = LocalDateTime.now();

    public void sendEmail(String emailAddress, String topic, String message) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(EMAIL_CONTAINER, true));
        writer.write(sendTime.format(formatter) + "\n");
        writer.write("Sent to: " + emailAddress + "\n");
        writer.write("Topic: " + topic + "\n");
        writer.write(message + "\n\n");
        writer.write("------------------------------------------------------\n\n");
        writer.close();
        LOG.info("Email to " + emailAddress + " sent.");
    }
}
