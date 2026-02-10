package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Bundesland;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.services.impl.BenutzerServiceImpl;
import com.ibosng.dbservice.services.impl.BundeslandServiceImpl;
import com.ibosng.dbservice.services.impl.PlzServiceImpl;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.PLZOrtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.ibosng.gatewayservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class PLZOrtServiceImpl implements PLZOrtService {

    private final PlzServiceImpl plzService;
    private final BundeslandServiceImpl bundeslandService;
    private final BenutzerDetailsService benutzerDetailsService;

    @Override
    public boolean addPLZAndOrtAssociation(TeilnehmerDto teilnehmerDto, String createdBy) {
        return savePLZAndOrtAssociation(teilnehmerDto.getPlz(), teilnehmerDto.getOrt(), createdBy);
    }

    @Override
    public PayloadResponse addPLZAndOrtAssociation(String plz, String ort, String token) {
        String createdBy = benutzerDetailsService.getUserFromToken(token).getEmail();
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(savePLZAndOrtAssociation(plz, ort, createdBy));
        return payloadResponse;
    }

    private boolean savePLZAndOrtAssociation(String plz, String ort, String createdBy) {
        log.info("Adding new plz and ort association");
        Integer plzInteger = parseStringToInteger(plz);
        if(plzInteger == null) {
            return false;
        }
        char firstDigit = plz.charAt(0);
        int bundeslandId = Character.getNumericValue(firstDigit);
        Bundesland bundesland = bundeslandService.findByPlzId(bundeslandId);
        if(bundesland != null) {
            log.info("Bundesland for new plz: {}", bundesland.getName());
            Plz newPlz = new Plz();
            newPlz.setPlz(plzInteger);
            newPlz.setOrt(ort);
            newPlz.setBundesland(bundesland);
            newPlz.setCreatedOn(LocalDateTime.now());
            newPlz.setCreatedBy(createdBy);
            plzService.save(newPlz);
            return true;
        }
        log.warn("Bundesland was null for plz: {}", plzInteger);
        return false;
    }
}
