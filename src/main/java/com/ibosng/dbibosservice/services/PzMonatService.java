package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.PzMonat;
import com.ibosng.dbibosservice.entities.PzMonatId;

import java.time.LocalDate;
import java.util.Optional;

public interface PzMonatService extends BaseService<PzMonat> {
    Optional<PzMonat> findById(PzMonatId id);

    PzMonat findByAdAdnrJahrAndMonat(Integer adAdnr, Integer jahr, Integer monat);

    String getMoxisStatus(Integer adAdnr, LocalDate month);
}
