package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Vorwahl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface VorwahlRepository extends JpaRepository<Vorwahl, Integer> {

    String FIND_BY_LAENDER = "select vor.vorwahl from Vorwahl vor where vor.land = :land";

    @Query(FIND_BY_LAENDER)
    List<Integer> findAllVorwahlByLaender(Land land);

    Vorwahl findByVorwahl(Integer vorwahl);
}
