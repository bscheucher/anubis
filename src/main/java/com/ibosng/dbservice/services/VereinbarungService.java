package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.VereinbarungDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface VereinbarungService extends BaseService<Vereinbarung> {

    List<Vereinbarung> findAllByPersonalnummer(Personalnummer personalnummer);
    Page<VereinbarungDto> findVereinbarungByCriteria(List<VereinbarungStatus> vereinbarungStatuses, String firmaName, String searchTerm, String sortBy, Sort.Direction direction, int page, int size);

    Page<VereinbarungDto> getAllVereinbarungenPageable(int page, int size);

    Vereinbarung findVereinbarungByWorkflow_Id(Integer workflowId);

    VereinbarungDto findVereinbarungById(Integer vereinbarungId);
}

