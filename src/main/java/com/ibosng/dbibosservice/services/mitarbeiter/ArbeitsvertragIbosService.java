package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.dtos.ContractDto;
import com.ibosng.dbibosservice.dtos.DienstortDto;
import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface ArbeitsvertragIbosService extends BaseService<ArbeitsvertragIbos> {
    List<ContractDto> getAllContracts(String user);
    List<ContractDto> getContracts(String user);
    List<ContractDto> getContractsWithoutLeistungen(String user);
    List<String> getAllDienstort();
    List<String> getAllKonstenstellen();
    List<DienstortDto> findDienstortByBezeichnung(String bezeichnung);
    List<ArbeitsvertragIbos> findAllByAddressNo(Integer addreseId);
    List<ArbeitsvertragIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after);
}
