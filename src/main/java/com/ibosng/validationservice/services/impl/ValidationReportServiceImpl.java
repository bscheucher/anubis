package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.entities.validations.Validations;
import com.ibosng.dbservice.services.impl.ValidationsServiceImpl;
import com.ibosng.microsoftgraphservice.services.MailService;

import com.ibosng.validationservice.services.ValidationReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ValidationReportServiceImpl implements ValidationReportService {

    private final ValidationsServiceImpl validationsService;
    private final MailService mailService;

    @Autowired
    public ValidationReportServiceImpl(ValidationsServiceImpl validationsService,
                                       MailService mailService) {
        this.validationsService = validationsService;
        this.mailService = mailService;
    }

    @Override
    public void createReport(String id, String subject, String body, String[] recipients) {
        List<Validations> validationsList = validationsService.findAllByIdentifier(id);
        if(!validationsList.isEmpty()) {
            mailService.sendEmail(subject, body, null, recipients);
        } else {
            log.info("No validation errors found, no error report was sent.");
        }
    }

    @Override
    public void createReport(String id, String identifier, String languageString, String[] recipients, Object[] subjectArgs, Object[] bodyArgs) {
        List<Validations> validationsList = validationsService.findAllByIdentifier(id);
        if(!validationsList.isEmpty()) {
            mailService.sendEmail(identifier, languageString, null, recipients, subjectArgs, bodyArgs);
        } else {
            log.info("No validation errors found, no error report was sent.");
        }
    }
}