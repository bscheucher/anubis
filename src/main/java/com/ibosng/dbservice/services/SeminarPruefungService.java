package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.SeminarPruefungDto;
import com.ibosng.dbservice.entities.seminar.SeminarPruefung;

import java.util.List;

public interface SeminarPruefungService extends BaseService<SeminarPruefung> {
    List<SeminarPruefung> findAllByTeilnehmerIdAndSeminarId(Integer teilnehmerId, Integer SeminarId);

    SeminarPruefungDto mapSeminarPruefungToDto(SeminarPruefung seminarPruefung);

}