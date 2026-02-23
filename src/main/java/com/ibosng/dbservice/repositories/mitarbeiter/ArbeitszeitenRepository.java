package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.mitarbeiter.Arbeitszeiten;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface ArbeitszeitenRepository extends JpaRepository<Arbeitszeiten, Integer> {

    @Query("select ars from Arbeitszeiten ars where ars.arbeitszeitenInfo.id = :arbeitszeiteninfoId")
    List<Arbeitszeiten> findByArbAndArbeitszeitenInfoId(Integer arbeitszeiteninfoId);

}
