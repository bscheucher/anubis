package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface ArbeitsvertragZusatzIbosService extends BaseService<ArbeitsvertragZusatzIbos> {
    List<ArbeitsvertragZusatzIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);
    List<ArbeitsvertragZusatzIbos> findAllByPersnr(String personalnummer);
    List<ArbeitsvertragZusatzIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after);
}
