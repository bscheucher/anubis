package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerStagingRepository extends JpaRepository<TeilnehmerStaging, Integer> {

    String DELETE_BY_IDENTIFIER = "delete from TeilnehmerStaging where importFilename = :identifier";

    List<TeilnehmerStaging> findAllByImportFilename(String importFilename);

    List<TeilnehmerStaging> findByImportFilenameAndStatusIn(String importFilename, List<TeilnehmerStatus> statuses);

    List<TeilnehmerStaging> findByImportFilenameAndTeilnehmerId(String filename, int teilnehmerId);

    List<TeilnehmerStaging> findFirstByImportFilenameAndTeilnehmerIdOrderByCreatedOnDesc(String filename, int teilnehmerId);

    List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatus(Integer teilnehmerId, TeilnehmerStatus status);

    List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatusOrderByCreatedOnDesc(Integer teilnehmerId, TeilnehmerStatus status);

    List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatusInOrderByCreatedOnDesc(Integer teilnehmerId, List<TeilnehmerStatus> statuses);

    List<TeilnehmerStaging> findAllByImportFilenameAndTeilnehmerIdAndStatus(String filename, int teilnehmerId, TeilnehmerStatus status);

    List<TeilnehmerStaging> findByTeilnehmerId(Integer teilnehmerId);

    List<TeilnehmerStaging> findByVornameAndNachname(String vorname, String nachname);

    List<TeilnehmerStaging> findByTeilnehmerIdAndSeminarIdentifierOrderByCreatedOnDesc(int teilnehmerId, String seminarIdentifier);

    @Modifying
    @Query(value = DELETE_BY_IDENTIFIER)
    void deleteByIdentifier(String identifier);
}
