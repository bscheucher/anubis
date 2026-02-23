package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.EmailTemplate;
import com.ibosng.dbservice.entities.Language;
import com.ibosng.dbservice.repositories.EmailTemplateRepository;
import com.ibosng.dbservice.services.EmailTemplateService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;

@Service
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;

    public EmailTemplateServiceImpl(EmailTemplateRepository emailTemplateRepository) {
        this.emailTemplateRepository = emailTemplateRepository;
    }

    @Override
    public List<EmailTemplate> findAll() {
        return emailTemplateRepository.findAll();
    }

    @Override
    public Optional<EmailTemplate> findById(Integer id) {
        return emailTemplateRepository.findById(id);
    }

    @Override
    public EmailTemplate save(EmailTemplate object) {
        return emailTemplateRepository.save(object);
    }

    @Override
    public List<EmailTemplate> saveAll(List<EmailTemplate> objects) {
        return emailTemplateRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        emailTemplateRepository.deleteById(id);
    }


    @Override
    public List<EmailTemplate> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public EmailTemplate findByIdentifierAndLanguage(String identifier, Language language) {
        List<EmailTemplate> emailTemplates = emailTemplateRepository.findAllByIdentifierAndLanguage(identifier, language);
        return findFirstObject(emailTemplates, new HashSet<>(Set.of(identifier)), "EmailTemplate");
    }

    @Override
    public String formatTemplate(String templateContent, Object... args) {
        return String.format(templateContent, args);
    }
}
