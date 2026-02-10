package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.ksttn.KsttnStatus;
import com.ibosng.dbibosservice.entities.teilnahme.Teilnahme;

public interface KsttnStatusService extends BaseService<KsttnStatus> {
    KsttnStatus findByTeilnahme(Teilnahme teilnahme);

    KsttnStatus findAllByIdKsttnkynr(Integer id);
}
