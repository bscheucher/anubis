package com.ibosng.dbservice.services.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.Auszahlungsantrag;
import com.ibosng.dbservice.entities.zeiterfassung.AuszahlungsantragStatus;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;
import java.util.Optional;

public interface AuszahlungsantragService extends BaseService<Auszahlungsantrag> {

    List<Auszahlungsantrag> findByStatus(AuszahlungsantragStatus status);
    Optional<Auszahlungsantrag> findByAnfrageNr(Integer anfrageNr);

}
