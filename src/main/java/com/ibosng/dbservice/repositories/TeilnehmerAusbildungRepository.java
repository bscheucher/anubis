package com.ibosng.dbservice.repositories;


import com.ibosng.dbservice.entities.teilnehmer.TnAusbildung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerAusbildungRepository extends JpaRepository<TnAusbildung, Integer> {
    List<TnAusbildung> findAllByTeilnehmerId(Integer teilnehmerId);
}
