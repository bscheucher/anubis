package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.TeilnehmerNotizDto;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotiz;

import java.util.List;

public interface TeilnehmerNotizService extends BaseService<TeilnehmerNotiz> {
    List<TeilnehmerNotiz> findAllByTeilnehmerId(Integer teilnehmerId);

    TeilnehmerNotizDto mapTeilnehmerNotizToDto(TeilnehmerNotiz teilnehmerNotiz);

}
