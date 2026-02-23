package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface ArbeitsvertragZusatzIbosRepository extends JpaRepository<ArbeitsvertragZusatzIbos, Integer> {

    List<ArbeitsvertragZusatzIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);
    List<ArbeitsvertragZusatzIbos> findAllByPersnr(String personalnummer);
    List<ArbeitsvertragZusatzIbos> findAllByEruserAndErdaAfterOrEruserAndAedaAfter(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda);
}
