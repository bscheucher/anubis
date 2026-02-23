package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungArt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface SeminarPruefungArtRepository extends JpaRepository<SeminarPruefungArt, Integer> {
    SeminarPruefungArt findByName(String name);
}
