package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.DvZusatzIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface DvZusatzIbosService extends BaseService<DvZusatzIbos> {
    List<DvZusatzIbos> findAllByAdAdnr(Integer adAdnr);

    List<DvZusatzIbos> findAllByAdAdnrAndDvNr(Integer adAdnr, Integer dvNr);

    List<DvZusatzIbos> findAllByDvNr(Integer dvNr);
    List<DvZusatzIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after);

}
