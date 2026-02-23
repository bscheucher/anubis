package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.seminar.SeminarPruefungInstitut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface SeminarPruefungInstitutRepository extends JpaRepository<SeminarPruefungInstitut, Integer> {
    SeminarPruefungInstitut findByName(String name);
}
