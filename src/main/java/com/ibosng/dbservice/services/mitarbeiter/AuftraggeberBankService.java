package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.AuftraggeberBank;
import com.ibosng.dbservice.services.BaseService;

import java.util.Optional;

public interface AuftraggeberBankService extends BaseService<AuftraggeberBank> {
    Optional<AuftraggeberBank> findByFirmenbankName(String firmenbankName);
}
