package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.PersonalbogenIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface PersonalbogenIbosRepository extends JpaRepository<PersonalbogenIbos, Integer> {
    List<PersonalbogenIbos> findAllByAdresseAdnr(Integer adresseAdnr);

    List<PersonalbogenIbos> findAllByPbEruserAndPbErdaAfterOrPbEruserAndPbAedaAfter(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda);
}
