package com.ibosng.microsoftgraphservice.services.impl;

import com.ibosng.dbservice.entities.EmailTemplate;
import com.ibosng.dbservice.entities.Language;
import com.ibosng.dbservice.services.EmailTemplateService;
import com.ibosng.dbservice.services.LanguageService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.AttachmentCollectionPage;
import com.microsoft.graph.requests.AttachmentCollectionResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Profile({"localdev", "test"})
@Service
@Slf4j
public class MailServiceImplLocal implements MailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServiceImplLocal.class);

    private final LanguageService languageService;
    private final EmailTemplateService emailTemplateService;

    public MailServiceImplLocal(
            LanguageService languageService,
            EmailTemplateService emailTemplateService
    ) {
        this.languageService = languageService;
        this.emailTemplateService = emailTemplateService;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Using local mock for MailService");
    }

    @Override
    public void sendEmail(String identifier, String languageString, List<File> files, String[] recipients, Object[] subjectArgs, Object[] bodyArgs) {
        Optional<Language> language = languageService.findByName(languageString);
        if (language.isEmpty()) {
            log.warn("Language {} not found, using default German", languageString);
            language = languageService.findByName("german");
        }
        EmailTemplate emailTemplate = emailTemplateService.findByIdentifierAndLanguage(identifier, language.get());
        if (emailTemplate != null) {
            // Format the subject and body with the provided arguments
            String subject = emailTemplateService.formatTemplate(emailTemplate.getSubject(), subjectArgs);
            String body = emailTemplateService.formatTemplate(emailTemplate.getBody(), bodyArgs);

            // Send the email with attachments
            sendEmail(subject, body, files, recipients);
        } else {
            log.error("Email template for identifier {} could not be found, unable to send the email", identifier);
        }
    }

    @Override
    public void sendEmail(String subject, String content, List<File> files, String[] recipients) {
        log.info("Sending email with subject: {}", subject);

        // Create the email message
        Message message = createMessageWithBody(subject, content);

        // Add attachments if present
        if (files != null && !files.isEmpty()) {
            try {
                addAttachment(message, files);
            } catch (IOException e) {
                log.error("Error attaching files: {}", e.getMessage());
            }
        }

        // Add recipients
        addRecipients(message, recipients);

        // Send the email
        sendMail(message);
    }

    private Message createMessageWithBody(String subject, String content) {
        Message message = new Message();
        message.subject = subject;
        ItemBody body = new ItemBody();
        body.contentType = BodyType.HTML;
        body.content = content;
        message.body = body;
        return message;
    }

    private void addRecipients(Message message, String[] recipientsString) {
        List<Recipient> recipients = new ArrayList<>();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = "ibosngbackend@local.dev";
        Recipient recipient = new Recipient();
        recipient.emailAddress = emailAddress;
        recipients.add(recipient);

        message.toRecipients = recipients;
    }

    private void addAttachment(Message message, List<File> files) throws IOException {
        LinkedList<Attachment> attachmentsList = new LinkedList<>();
        // Create and configure the attachment
        for (File file : files) {
            FileAttachment attachment = new FileAttachment();
            attachment.oDataType = "#microsoft.graph.fileAttachment";
            attachment.name = file.getName();
            attachment.contentType = Files.probeContentType(file.toPath()); // Determine the MIME type from the file
            attachment.contentBytes = Files.readAllBytes(file.toPath());
            attachmentsList.add(attachment);
        }
        AttachmentCollectionResponse attachmentCollectionResponse = new AttachmentCollectionResponse();
        attachmentCollectionResponse.value = attachmentsList;

        message.attachments = new AttachmentCollectionPage(attachmentCollectionResponse, null);
    }

    private void sendMail(Message message) {
        // Feel free to extend this for better output during development
        var recipients = Optional.ofNullable(message.toRecipients)
                .orElse(new ArrayList<>())
                .stream()
                .map(
                        recipient -> Optional.ofNullable(recipient.emailAddress)
                                .map(emailAddress -> emailAddress.address)
                                .orElse("")
                )
                .toArray();
        LOGGER.info("Sending email to {} with subject '{}'", recipients, message.subject);
        LOGGER.info("Email body {}", Optional.ofNullable(message.body).map(body -> body.content).orElse(""));
    }

}
