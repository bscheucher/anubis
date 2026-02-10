package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.PersonalbogenIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface PersonalbogenIbosService extends BaseService<PersonalbogenIbos> {
    List<PersonalbogenIbos> findAllByAdresseAdnr(Integer adresseAdnr);
    List<PersonalbogenIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after);
}
