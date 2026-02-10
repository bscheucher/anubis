package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.TnZertifikatDto;
import com.ibosng.dbservice.entities.teilnehmer.TnZertifikat;

import java.util.List;

public interface TeilnehmerZertifikatService extends BaseService<TnZertifikat> {
    List<TnZertifikat> findAllByTeilnehmerId(Integer teilnehmerId);

    TnZertifikatDto mapTeilnehmerZertifikatToDto(TnZertifikat tnZertifikat);

}
