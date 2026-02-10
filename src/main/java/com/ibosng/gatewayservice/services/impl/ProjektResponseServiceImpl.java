package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.services.ProjektService;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.ProjektResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjektResponseServiceImpl implements ProjektResponseService {

    private final ProjektService projektService;

    @Override
    public PayloadResponse getProjektByStatus(Boolean isActive, Boolean isKorrigieren, Benutzer benutzer) {
        List<String> projekts = projektService.findByStatus(isActive, isKorrigieren, benutzer == null ? null : benutzer.getId());
        PayloadTypeList<String> projektDtoPayloadTypeList = new PayloadTypeList<>(PayloadTypes.PROJEKT.getValue(), projekts);
        return PayloadResponse.builder()
                .success(true)
                .data(List.of(projektDtoPayloadTypeList))
                .build();
    }
}
