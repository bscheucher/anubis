package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.teilnehmer.TeilnahmeReason;

public interface TeilnahmeReasonService extends BaseService<TeilnahmeReason> {
    TeilnahmeReason findByKuerzel(String kuerzel);
}
