package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.KvVerwendungsgruppeIbos;

import java.util.List;

public interface KvVerwendungsgruppeIbosService extends BaseService<KvVerwendungsgruppeIbos> {
    List<KvVerwendungsgruppeIbos> findAllByKollektivvertragIdAndBezeichnung(Integer kollektivvertragId, String bezeichnung);
    List<KvVerwendungsgruppeIbos> findAllByKollektivvertragIdAndBezeichnungAndBmdId(Integer kollektivvertragId, String bezeichnung, Integer bmdId);
}
