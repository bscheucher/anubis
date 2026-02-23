package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerRepositoryExtended {

    List<Teilnehmer> findTeilnehmerFiltered1(String identifiersString,
                                             String seminarName,
                                             String projektName,
                                             Boolean isActive,
                                             Boolean isUebaTeilnehmer,
                                             Boolean isAngemeldet,
                                             String geschlecht,
                                             Boolean isFehlerhaft,
                                             String massnahmennummer,
                                             Integer benutzerId,
                                             String sortProperty,
                                             String direction,
                                             int page,
                                             int size);

    Page<Teilnehmer> findTeilnehmerFiltered(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Benutzer benutzer, String sortProperty, Sort.Direction direction, int page, int size);

}
