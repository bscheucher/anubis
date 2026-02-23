package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.dtos.zeiterfassung.umbuchung.UmbuchungDto;
import com.ibosng.gatewayservice.dtos.GenehmigungDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import org.springframework.data.domain.Sort;


public interface ZeiterfassungGatewayService {

    PayloadResponse deleteAbwesenheit(Integer abwesenheitId);

    PayloadResponse resendAbwesenheit(Integer id, String token);

    PayloadResponse saveAbwesenheit(AbwesenheitDto abwesenheitDto, String token);

    PayloadResponse getAbwesenheit(Integer abwesenheitId);

    PayloadResponse getAbwesenheitenList(String token, Boolean isPersonal, String status, Integer year, String sortProperty, String sortDirection, int page, int size);

    PayloadResponse getMitarbeiterZeitbuchungen(String token, String startDate, String endDate, boolean shouldSync);

    PayloadResponse deleteZeitausgleich(Integer personalnummerId, String date);

    PayloadResponse genehmigungAbwesenheit(String token, Integer abwesenheitId, GenehmigungDto genehmigung);

    void syncAbwesenheitData();

    void syncDocumentData();

    void closeMonaten();

    PayloadResponse getAbwesenheitOverview(String token, String startDate, String endDate, String sortProperty, Sort.Direction sortDirection, int page, int size);

    PayloadResponse getUmbuchung(String token, String date);

    PayloadResponse postUmbuchung(String token, String date, UmbuchungDto umbuchungDto);
}
