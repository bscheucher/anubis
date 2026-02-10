package com.ibosng.dbservice.repositories;


import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerNotizRepository extends JpaRepository<TeilnehmerNotiz, Integer> {
    List<TeilnehmerNotiz> findAllByTeilnehmerId(Integer teilnehmerId);
}
