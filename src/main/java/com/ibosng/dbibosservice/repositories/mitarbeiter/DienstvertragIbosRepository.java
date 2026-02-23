package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.DienstvertragIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface DienstvertragIbosRepository extends JpaRepository<DienstvertragIbos, Integer> {
    List<DienstvertragIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);
    List<DienstvertragIbos> findAllByDvEruserAndDvErdaAfterOrDvEruserAndDvAedaAfter(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda);
}
