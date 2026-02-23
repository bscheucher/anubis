package com.ibosng.validationservice.services;

import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface KVEinstufungService {

    ResponseEntity<WWorkflowStatus> calculateKVEinstufung(String personalNummer, Boolean isOnboarding);
    int calculateTotalMonths(List<Vordienstzeiten> vordienstzeitens);
}
