package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungGegenstand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface SeminarPruefungGegenstandRepository extends JpaRepository<SeminarPruefungGegenstand, Integer> {
    SeminarPruefungGegenstand findByName(String name);
}
