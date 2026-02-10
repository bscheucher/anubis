package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.teilnehmer.TnAusbildungType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerAusbildungTypeRepository extends JpaRepository<TnAusbildungType, Integer> {

    TnAusbildungType findByName(String name);
}
