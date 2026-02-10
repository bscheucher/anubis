package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.EmailTemplate;
import com.ibosng.dbservice.entities.Language;

public interface EmailTemplateService extends BaseService<EmailTemplate> {

    EmailTemplate findByIdentifierAndLanguage(String identifier, Language language);
    String formatTemplate(String templateContent, Object... args);
}
