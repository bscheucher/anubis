package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.InternationalPlz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InternationalPlzRepository extends JpaRepository<InternationalPlz, Integer> {

    String GET_ALL_PLZ = "select pl.plz from InternationalPlz pl";
    String GET_ALL_ORT = "select pl.ort from InternationalPlz pl";
    String GET_PLZ_FROM_ORT = "select pl.plz from InternationalPlz pl where pl.ort = :ort";
    String GET_ORT_FROM_PLZ = "select pl.ort from InternationalPlz pl where pl.plz = :plz";
    String GET_PLZ_BY_PLZ_ORT_LAND = "select pl from InternationalPlz pl where pl.plz = :plz and pl.ort = :ort and pl.land.id = :land";

    @Query(value = GET_ALL_PLZ)
    List<String> getAllPlz();

    @Query(value = GET_ALL_ORT)
    List<String> getAllOrt();

    List<InternationalPlz> findByPlz(String plz);

    @Query(GET_ORT_FROM_PLZ)
    List<String> findOrtByPlz(String plz);

    @Query(GET_PLZ_FROM_ORT)
    String findPlzByOrt(String ort);

    @Query(GET_PLZ_BY_PLZ_ORT_LAND)
    Optional<InternationalPlz> findPlzByPlzOrtLand(String plz, String ort, Integer land);
}
