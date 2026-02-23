package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Land;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface LandRepository extends JpaRepository<Land, Integer> {
    List<Land> findByTelefonvorwahl(String vorwahl);
    List<Land> findAllByEldaCode(String eldaCode);
    Land findByLandName(String landname);
    List<Land> findByIsInEuEeaCh(Boolean isIn);
    List<Land> findByLandCode(String landcode);
    List<Land> findAllByLhrKz(String lhrKz);
}
