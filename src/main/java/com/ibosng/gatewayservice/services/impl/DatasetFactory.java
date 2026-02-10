package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.SeminarService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DatasetFactory {

    private final BenutzerService benutzerService;
    private final SeminarService seminarService;

    private static final String BENUTZER_KEY = "benutzer";
    private static final String SEMINAR_KEY = "seminar";

    public DatasetFactory(BenutzerService benutzerService, SeminarService seminarService) {
        this.benutzerService = benutzerService;
        this.seminarService = seminarService;
    }

    public List<Object> getDataSet(String dataKey){
        if(dataKey.equals(BENUTZER_KEY)){
            return Collections.singletonList(benutzerService.findAll());
        }
        else if(dataKey.equals(SEMINAR_KEY)){
            return Collections.singletonList(seminarService.findAll());
        }

        return null;
    }
}
