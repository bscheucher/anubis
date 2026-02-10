package com.ibosng.validationservice.services;

public interface ValidationReportService {
    void createReport(String id, String subject, String body, String[] recipients);
    void createReport(String id, String identifier, String languageString, String[] recipients, Object[] subjectArgs, Object[] bodyArgs);
}
