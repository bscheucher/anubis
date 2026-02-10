package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragFixIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.util.List;

public interface ArbeitsvertragFixIbosService extends BaseService<ArbeitsvertragFixIbos> {
    List<ArbeitsvertragFixIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);

    List<String> findFuehrungskraftByPersnr(String persnr);

    List<String> findFuehrungskraftUPNsByPersnr(String persnr);
}
