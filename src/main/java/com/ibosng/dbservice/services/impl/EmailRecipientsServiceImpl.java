package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.EmailRecipient;
import com.ibosng.dbservice.repositories.EmailRecipientRepository;
import com.ibosng.dbservice.services.EmailRecipientsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Service
@RequiredArgsConstructor
public class EmailRecipientsServiceImpl implements EmailRecipientsService {

    private final EmailRecipientRepository emailRecipientRepository;

    @Override
    public List<EmailRecipient> findAll() {
        return emailRecipientRepository.findAll();
    }

    @Override
    public Optional<EmailRecipient> findById(Integer id) {
        return emailRecipientRepository.findById(id);
    }

    @Override
    public EmailRecipient save(EmailRecipient object) {
        return emailRecipientRepository.save(object);
    }

    @Override
    public List<EmailRecipient> saveAll(List<EmailRecipient> objects) {
        return emailRecipientRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        emailRecipientRepository.deleteById(id);
    }

    @Override
    public List<EmailRecipient> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public List<EmailRecipient> findOrSave(List<String> recipients) {
        List<EmailRecipient> emailRecipients = new ArrayList<>();
        for (String recipient : recipients) {
            EmailRecipient emailRecipient = emailRecipientRepository.findByEmail(recipient);
            if (emailRecipient == null) {
                emailRecipient = new EmailRecipient();
                emailRecipient.setEmail(recipient);
                emailRecipient.setCreatedOn(getLocalDateNow());
                emailRecipients.add(emailRecipientRepository.save(emailRecipient));
            } else {
                emailRecipients.add(emailRecipient);
            }
        }
        return emailRecipients;
    }
}
