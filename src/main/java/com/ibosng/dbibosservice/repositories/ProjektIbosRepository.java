package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.ProjektIbos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface ProjektIbosRepository extends JpaRepository<ProjektIbos, Integer> {

    Page<ProjektIbos>  findAllByPjErdaAfterOrPjAedaAfter(LocalDateTime timeErda, LocalDateTime timeAeda, Pageable pageable);

    Page<ProjektIbos> findAllByPjEruserAndPjErdaOrPjEruserAndPjAeda(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda, Pageable pageable);

    @Query(value = "SELECT pr FROM ProjektIbos pr where pr.pjDatumVon <= current_date and pr.pjDatumBis >= current_date")
    List<ProjektIbos> fingAllActiveProjektIbos();
}
