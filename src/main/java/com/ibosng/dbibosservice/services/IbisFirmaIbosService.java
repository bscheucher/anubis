package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.IbisFirmaIbos;

public interface IbisFirmaIbosService extends BaseService<IbisFirmaIbos> {
    IbisFirmaIbos findByBmdKlientIdAndLhrKzAndLhrNr(Integer bmdKlientId, String lhrKz, Integer lhrNr);
}
