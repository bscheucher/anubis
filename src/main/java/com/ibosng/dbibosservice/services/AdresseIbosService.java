package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AdresseIbosService extends BaseService<AdresseIbos> {
    Page<AdresseIbos> findAllChangedAfterPageable(LocalDateTime after, Pageable pageable);

    List<AdresseIbos> findAllByAdaedaAfterAndAdtyp(LocalDateTime after, String type);

    Page<AdresseIbos> findAllByAderuser(String createdBy, Pageable pageable);

    List<AdresseIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after);

    List<AdresseIbos> findAllByChangedAfterAndAderuserAndType(String createdBy, LocalDateTime after, String type);

    List<AdresseIbos> findAllByAdpersnrAndAderuser(String personalnummer, String eruser);

    List<AdresseIbos> findAllByVornameAndNachname(String vorname, String nachname);

    List<AdresseIbos> findAllByAdemail1(String email);

    List<AdresseIbos> findAllByAdemail2(String email);

    List<AdresseIbos> getFilteredTeilnehmer(String searchTerm, String sortBy, String sortDir);

    List<AdresseIbos> findValidAdresseFromWorkEmail(String workEmail);

    List<AdresseIbos> findAdresseIbosFromPersonalnummer(String personalnummer, Integer ibisFirma);

    Integer getAdresseIdFromPersonalnummer(String personalnummer, Integer ibisFirma);

    Optional<AdresseIbos> findByFirstNameAndLastNameAndSvnrAndCreationUser(String firstName, String lastName, String svnr, String createdBy);

    List<String> findEmailsOfActiveFuehrungskraefte();

    List<String> findEmailsOfActiveStartcoaches();

    List<AdresseIbos> findValidAdresseFromWorkEmailAndVertragFix(String workEmail);

    String getFuehrungskraftFromEmail(String email);

    String getFuehrungskraftFromLogin(String login);

    String getFuehrungskraftUPNFromLogin(String login);

    String getAdresseFromUPN(String upn);

    List<AdresseIbos> findAllByChangedAfterWithSeminarData(String createdBy, LocalDateTime after, String type);

    String findKostenstelle2UpnByKostenstelleId(int id);
}
