package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.pzleistung.Pzleistung;
import com.ibosng.dbibosservice.entities.pzleistung.PzleistungId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface PzleistungRepository extends JpaRepository<Pzleistung, PzleistungId> {

    @Query("SELECT p FROM Pzleistung p WHERE p.id.ADadnr = :adadnr  " +
            "AND p.lztyp = 'l' " +
            "AND (p.lzaeda BETWEEN :startDate AND :endDate OR p.lzerda BETWEEN :startDate AND :endDate)")
    List<Pzleistung> findById_ADadnrAndLzaedaBetweenOrLzerdaBetween(@Param("adadnr") Integer adadnr,
                                                                    @Param("startDate") LocalDateTime startDate,
                                                                    @Param("endDate") LocalDateTime endDate);

    List<Pzleistung> findById_ADadnrAndLzdatumBetweenAndLztyp(Integer ADadnr, LocalDate lzdatumStart, LocalDate lzdatumEnd, String lztyp);

    Page<Pzleistung> findById_ADadnrAndLzdatumBetweenAndLztyp(Integer ADadnr, LocalDate lzdatumStart, LocalDate lzdatumEnd, String lztyp, Pageable pageable);

    Page<Pzleistung> findById_ADadnrInAndLzdatumBetweenAndLztyp(Collection<Integer> ADadnrs, LocalDate lzdatumStart, LocalDate lzdatumEnd, String lztyp, Pageable pageable);
}
