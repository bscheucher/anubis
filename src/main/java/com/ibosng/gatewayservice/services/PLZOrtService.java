package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;

public interface PLZOrtService {
    boolean addPLZAndOrtAssociation(TeilnehmerDto teilnehmerDto, String createdBy);

    PayloadResponse addPLZAndOrtAssociation(String plz, String ort, String token);
}
