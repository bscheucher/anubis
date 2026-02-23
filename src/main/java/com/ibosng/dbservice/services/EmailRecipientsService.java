package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.EmailRecipient;

import java.util.List;

public interface EmailRecipientsService extends BaseService<EmailRecipient> {
    List<EmailRecipient> findOrSave(List<String> recipients);
}
