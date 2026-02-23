package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.smad.SmAd;
import com.ibosng.dbibosservice.entities.smad.SmAdId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface SmAdRepository extends JpaRepository<SmAd, SmAdId> {
    List<SmAd> findAllBySmAdId_SeminarSmnr(Integer seminarNummer);
}
