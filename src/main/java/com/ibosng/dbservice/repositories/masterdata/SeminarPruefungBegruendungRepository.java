package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungBegruendung;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface SeminarPruefungBegruendungRepository extends JpaRepository<SeminarPruefungBegruendung, Integer> {
    SeminarPruefungBegruendung findByName(String name);
}
