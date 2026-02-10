package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.entities.teilnehmer.TnBerufserfahrung;

import java.util.List;

public interface TeilnehmerBerufserfahrungService extends BaseService<TnBerufserfahrung> {
    List<TnBerufserfahrung> findAllByTeilnehmerId(Integer teilnehmerId);

    TnBerufserfahrungDto mapTeilnehmerBerufserfahrungToDto(TnBerufserfahrung tnBerufserfahrung);

    TnBerufserfahrung findByTeilnehmerIdAndBeruf(Integer teilnehmerId, Integer berufId);
}
