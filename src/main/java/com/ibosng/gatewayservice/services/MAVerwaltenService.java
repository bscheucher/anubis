package com.ibosng.gatewayservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungMultiRequestDto;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.ReportResponse;
import com.ibosng.gatewayservice.enums.Action;
import com.ibosng.gatewayservice.enums.UserType;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MAVerwaltenService {

    PayloadResponse findMAByCriteria(String searchTerm, String wohnort, List<String> firmen, List<String> kostenstellen, List<String> beschaeftigungstatusen, List<String> jobbezeichnungen, List<String> kategorien, String sortBy, String direction, int page, int size);

    PayloadResponse findVertragsaenderungenByCriteria(String searchTerm, List<String> statuses, String sortBy, String direction, int page, int size);

    PayloadResponse performVertragsaenderung(VertragsaenderungMultiRequestDto vertragsaenderungMultiRequestDto, String token);

    PayloadResponse getVertragsaenderung(Integer vertragsaenderungId);

    ResponseEntity<PayloadResponse> acceptVertragsaenderung(Action action, UserType userType, Integer id, VertragsaenderungMultiRequestDto vertragsaenderungMultiRequestDto, String authorizationHeader);

    PayloadResponse saveStammdaten(StammdatenDto stammdatenDto, String token);

    PayloadResponse saveVertragsdaten(VertragsdatenDto vertragsDatenDto, String token);

    PayloadResponse saveVordienstzeiten(VordienstzeitenDto vordienstzeitenDto, String token);

    PayloadResponse saveUnterhaltsberechtigte(UnterhaltsberechtigteDto unterhaltsberechtigteDto, String token);

    ReportResponse downloadVertragsaenderungFile(String personalnummer);
}
