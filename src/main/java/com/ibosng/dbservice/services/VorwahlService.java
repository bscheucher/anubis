package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.Vorwahl;

import java.util.List;

public interface VorwahlService extends BaseService<Vorwahl> {
    List<Integer> findAllVorwahlByLaender(Land land);

    Vorwahl findByVorwahl(Integer vorwahl);
}
