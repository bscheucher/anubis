package com.ibosng.microsoftgraphservice.services.impl;

import com.ibosng.dbservice.entities.EmailTemplate;
import com.ibosng.dbservice.entities.Language;
import com.ibosng.dbservice.services.EmailTemplateService;
import com.ibosng.dbservice.services.LanguageService;
import com.ibosng.microsoftgraphservice.config.properties.MailProperties;
import com.ibosng.microsoftgraphservice.services.MSEnvironmentService;
import com.ibosng.microsoftgraphservice.services.MailService;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.AttachmentCollectionPage;
import com.microsoft.graph.requests.AttachmentCollectionResponse;
import com.microsoft.graph.requests.GraphServiceClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Profile("!(localdev | test)")
@Service
@Slf4j
public class MailServiceImpl implements MailService {

    @Getter
    @Value("${mailRecipients:#{null}}")
    private String mailRecipients;

    private final GraphServiceClient<Request> graphClient;
    private final LanguageService languageService;
    private final EmailTemplateService emailTemplateService;
    private final MailProperties mailProperties;
    private final MSEnvironmentService msEnvironmentService;

    public MailServiceImpl(@Qualifier("mailGraphClient") GraphServiceClient<Request> graphClient,
                           LanguageService languageService,
                           EmailTemplateService emailTemplateService,
                           MailProperties mailProperties,
                           MSEnvironmentService msEnvironmentService) {
        this.graphClient = graphClient;
        this.languageService = languageService;
        this.emailTemplateService = emailTemplateService;
        this.mailProperties = mailProperties;
        this.msEnvironmentService = msEnvironmentService;
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
        // Add recipients
        List<Recipient> recipients = new ArrayList<>();
        if (msEnvironmentService.isProduction()) {
            for (String recipientEmail : recipientsString) {
                EmailAddress emailAddress = new EmailAddress();
                emailAddress.address = recipientEmail;
                Recipient recipient = new Recipient();
                recipient.emailAddress = emailAddress;
                recipients.add(recipient);
            }
        } else {
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.address = getMailRecipients();
            Recipient recipient = new Recipient();
            recipient.emailAddress = emailAddress;
            recipients.add(recipient);
        }

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
        this.graphClient.users(mailProperties.getUserId()).sendMail(UserSendMailParameterSet
                        .newBuilder()
                        .withMessage(message)
                        .withSaveToSentItems(true)
                        .build())
                .buildRequest()
                .post();
    }

}
