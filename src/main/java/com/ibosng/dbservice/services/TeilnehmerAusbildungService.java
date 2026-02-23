package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.TnAusbildungDto;
import com.ibosng.dbservice.entities.teilnehmer.TnAusbildung;

import java.util.List;

public interface TeilnehmerAusbildungService extends BaseService<TnAusbildung> {
    List<TnAusbildung> findAllByTeilnehmerId(Integer teilnehmerId);

    TnAusbildungDto mapTeilnehmerAusbildungToDto(TnAusbildung tnAusbildung);
}
