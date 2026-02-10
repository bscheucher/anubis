package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;

import java.util.List;

public interface TeilnehmerStagingService extends BaseService<TeilnehmerStaging> {
    List<TeilnehmerStaging> findByImportFilenameAndStatusIn(String importFilename, List<TeilnehmerStatus> statuses);

    void deleteAllByIdentifier(String identifier);

    List<TeilnehmerStaging> findByImportFilenameAndTeilnehmerId(String filename, int teilnehmerId);

    List<TeilnehmerStaging> findAllByImportFilenameAndTeilnehmerIdAndStatus(String filename, int teilnehmerId, TeilnehmerStatus status);

    List<TeilnehmerStaging> findByTeilnehmerId(Integer teilnhemherId);

    List<TeilnehmerDto> mapTeilnehmer2TeilnehmerDto(Teilnehmer teilnehmer, boolean isFiltered, boolean isSeminarPresent, String seminarIdentifier, boolean isFromValidationService);

    List<TeilnehmerStaging> findByVornameAndNachname(String vorname, String nachname);

    List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatus(Integer teilnehmerId, TeilnehmerStatus status);

    void mapTeilnehmerToDto(Teilnehmer teilnehmer, TeilnehmerDto dto, boolean isSeminarPresent);

    List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatusOrderByCreatedOnDesc(Integer teilnehmerId, TeilnehmerStatus status);

    List<TeilnehmerStaging> findAllByTeilnehmerIdAndStatusInOrderByCreatedOnDesc(Integer teilnehmerId, List<TeilnehmerStatus> statuses);

    List<TeilnehmerStaging> findFirstByImportFilenameAndTeilnehmerIdOrderByCreatedOnDesc(String filename, int teilnehmerId);

    List<TeilnehmerStaging> findByTeilnehmerIdAndSeminarIdentifierOrderByCreatedOnDesc(int teilnehmerId, String seminarIdentifier);
}
