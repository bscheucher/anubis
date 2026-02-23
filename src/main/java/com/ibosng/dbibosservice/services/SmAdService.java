package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.smad.SmAd;

import java.util.List;

public interface SmAdService extends BaseService<SmAd> {
    List<SmAd> findAllBySeminarNummer(Integer seminarNummer);
}
