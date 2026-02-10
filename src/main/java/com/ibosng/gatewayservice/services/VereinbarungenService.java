package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.VereinbarungDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import com.ibosng.gatewayservice.dtos.ReportRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface VereinbarungenService {

    PayloadResponse listAll(int page, int size);
    PayloadResponse listVereinbarungenForBenutzer(Benutzer benutzer, int page, int size);
    PayloadResponse getFilteredVereinbarungen(List<VereinbarungStatus> vereinbarungStatuses, String firma, String searchTerm, String sortProperty, String sortDirection, int page, int size);
    PayloadResponse getFilteredVereinbarungenForBenutzer(Benutzer benutzer, List<VereinbarungStatus> vereinbarungStatuses, String firma, String searchTerm, String sortProperty, String sortDirection, int page, int size);

    PayloadResponse getVereinbarung(Integer vereinbarungId);

    PayloadResponse updateVereinbarung(VereinbarungDto vereinbarungDto);

    PayloadResponse createVereinbarung(ReportRequestDto reportRequestDto);

    PayloadResponse persistVereinbarungFile(VereinbarungDto vereinbarungDto);

    PayloadResponse getWorkflowgroup(Integer vereinbarungId);

    ReportResponse generateVereinbarungPreview(VereinbarungDto vereinbarungDto);
}
