package com.ibosng.dbservice.repositories;


import com.ibosng.dbservice.entities.teilnehmer.TnBerufserfahrung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerBerufserfahrungRepository extends JpaRepository<TnBerufserfahrung, Integer> {
    List<TnBerufserfahrung> findAllByTeilnehmerId(Integer teilnehmerId);

    TnBerufserfahrung findByTeilnehmer_IdAndBeruf_Id(Integer teilnehmerId, Integer berufId);
}
