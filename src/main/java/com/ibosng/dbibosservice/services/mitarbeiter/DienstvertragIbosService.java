package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.DienstvertragIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface DienstvertragIbosService extends BaseService<DienstvertragIbos> {
    List<DienstvertragIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);
    List<DienstvertragIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after);
}
