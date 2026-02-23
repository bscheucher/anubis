package com.ibosng.dbservice.repositories.natif;

import com.ibosng.dbservice.entities.natif.Kompetenz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface KompetenzenRepository extends JpaRepository<Kompetenz, Integer> {
    List<Kompetenz> findByTeilnehmerId(Integer teilnehmerId);

    boolean existsByTeilnehmer_IdAndArtAndConfidenceNameAndScoreAndConfidenceScore(Integer id, String art,
                                                                                   Double confidenceName,
                                                                                   BigDecimal score,
                                                                                   Double confidenceScore);

    boolean existsByTeilnehmer_IdAndArtAndNameAndConfidenceNameAndScoreAndConfidenceScoreAndPinCodeAndConfidencePinCodeAndTagesdatumAndConfidenceTagesdatum
            (Integer id, String art, String name, Double confidenceName, BigDecimal score, Double confidenceScore,
             String pinCode, Double confidencePinCode, LocalDate tagesdatum, Double confidenceTagesdatum);

    @Modifying
    @Query("DELETE FROM Kompetenz k WHERE k.teilnehmer.id = :teilnehmerId")
    void deleteByTeilnehmerId(@Param("teilnehmerId") Integer teilnehmerId);
}
