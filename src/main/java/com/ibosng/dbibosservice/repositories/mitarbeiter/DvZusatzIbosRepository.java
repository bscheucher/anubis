package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.DvZusatzIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface DvZusatzIbosRepository extends JpaRepository<DvZusatzIbos, Integer> {
    List<DvZusatzIbos> findAllByAdAdnr(Integer adAdnr);

    List<DvZusatzIbos> findAllByAdAdnrAndDvNr(Integer adAdnr, Integer dvNr);

    List<DvZusatzIbos> findAllByDvNr(Integer dvNr);
    List<DvZusatzIbos> findAllByDzEruserAndDzErdaAfterOrDzEruserAndDzAedaAfter(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda);
}
