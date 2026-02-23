package com.ibosng.microsoftgraphservice.services;

import java.io.File;
import java.util.List;

public interface MailService {
    void sendEmail(String subject, String content, List<File> files, String[] recipients);
    void sendEmail(String identifier, String languageString, List<File> files, String[] recipients, Object[] subjectArgs, Object[] bodyArgs);
}
