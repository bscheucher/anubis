package com.ibosng.dbservice.services.lhr;

import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.entities.lhr.Abmeldung;
import com.ibosng.dbservice.entities.lhr.AbmeldungStatus;
import com.ibosng.dbservice.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AbmeldungService extends BaseService<Abmeldung> {

    Page<AbmeldungDto> findAll(Pageable pageable);

    AbmeldungDto findByTeilnehmerId(Integer teilnehmerId);

    AbmeldungDto mapToAbmeldungDto(Abmeldung abmeldung);

    Abmeldung mapToAbmeldung(AbmeldungDto abmeldungDto, AbmeldungStatus status);

    Abmeldung findAllBySvNummer(String svNummer);

    Optional<Abmeldung> findAllTeilnehmerID(Integer teilnehmerId);
}
