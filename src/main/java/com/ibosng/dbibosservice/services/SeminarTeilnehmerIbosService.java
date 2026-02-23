package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.smtn.SeminarTeilnehmerIbos;
import com.ibosng.dbibosservice.entities.smtn.SmTnId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeminarTeilnehmerIbosService extends BaseService<SeminarTeilnehmerIbos> {

    void deleteById(SmTnId id);

    List<SeminarTeilnehmerIbos> findByAdresseNr(Integer adresseNr);

    Optional<SeminarTeilnehmerIbos> findById(SmTnId id);

    SeminarTeilnehmerIbos findByAdresseAndSeminar(Integer adresse, Integer seminar);

    List<SeminarTeilnehmerIbos> findAllByTaeruserAndTaerdaAfterOrTaeruserAndTaaeda(String taeruser, LocalDateTime taerdaAfter, String taeruser1, LocalDateTime taaeda);
}
