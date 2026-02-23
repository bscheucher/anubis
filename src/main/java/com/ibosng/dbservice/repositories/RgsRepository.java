package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Rgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface RgsRepository extends JpaRepository<Rgs, Integer> {

    String GET_ALL_RGS = "select rg.rgs from Rgs rg";

    @Query(value = GET_ALL_RGS)
    List<Integer> getAllRgs();

    Rgs findByRgs(Integer rgs);
}
