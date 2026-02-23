package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.dtos.ZeitausgleichDto;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LHRZeiterfassungService {

    ResponseEntity<ZeiterfassungTransferDto> sendZeiterfassungTransfer(String zeiterfassungTransferId, String createdBy);

    ResponseEntity<List<ZeitausgleichDto>> submitZeitausgleichForPeriod(AbwesenheitDto abwewsenheitenDto);
}
