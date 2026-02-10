package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.ProjektIbos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjektIbosService extends BaseService<ProjektIbos> {
    Page<ProjektIbos> findAllChangedAfterPageable(LocalDateTime after, Pageable pageable);

    Page<ProjektIbos> findAllCreatedByAndChangedAfterPageable(String createdBy, LocalDateTime after, Pageable pageable);

    List<ProjektIbos> fingAllActiveProjektIbos();
}
