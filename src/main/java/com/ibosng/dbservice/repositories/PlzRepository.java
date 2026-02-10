package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Plz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface PlzRepository extends JpaRepository<Plz, Integer> {

    String GET_ALL_PLZ = "select pl.plz from Plz pl";
    String GET_ALL_ORT = "select pl.ort from Plz pl";
    String GET_PLZ_FROM_ORT = "select pl.plz from Plz pl where pl.ort = :ort";
    String GET_ORT_FROM_PLZ = "select pl.ort from Plz pl where pl.plz = :plz";

    @Query(value = GET_ALL_PLZ)
    List<Integer> getAllPlz();

    @Query(value = GET_ALL_ORT)
    List<String> getAllOrt();

    List<Plz> findByPlz(Integer plz);

    @Query(GET_ORT_FROM_PLZ)
    List<String> findOrtByPlz(Integer plz);

    @Query(GET_PLZ_FROM_ORT)
    Integer findPlzByOrt(String ort);

}
