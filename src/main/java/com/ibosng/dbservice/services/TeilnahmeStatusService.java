package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.teilnahme.TeilnehmerTeilnahmeOverviewDto;
import com.ibosng.dbservice.entities.teilnehmer.TeilnahmeStatus;
import com.ibosng.dbservice.entities.teilnehmer.TeilnahmerTeilnahme;

import java.util.List;

public interface TeilnahmeStatusService extends BaseService<TeilnahmeStatus> {
    List<TeilnahmeStatus> formByTnTeilnahme(TeilnahmerTeilnahme teilnahmerTeilnahme);

    List<TeilnahmeStatus> findByTnTeilnahme(int id);

    TeilnehmerTeilnahmeOverviewDto map(TeilnahmeStatus teilnahmeStatus);

    TeilnahmeStatus findByTeilnehmerAndTeilnahme(int teilnahmerId, int tnTeilnahmeId);
}
