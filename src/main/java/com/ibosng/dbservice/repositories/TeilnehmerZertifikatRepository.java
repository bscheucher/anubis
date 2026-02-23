package com.ibosng.dbservice.repositories;


import com.ibosng.dbservice.entities.teilnehmer.TnZertifikat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerZertifikatRepository extends JpaRepository<TnZertifikat, Integer> {
    List<TnZertifikat> findAllByTeilnehmerId(Integer teilnehmerId);
}
