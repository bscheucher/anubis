package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Plz;

import java.util.List;

public interface PlzService extends BaseService<Plz> {
    List<Integer> getAllPlz();

    List<String> getAllOrt();

    List<Plz> findByPlz(Integer Plz);

    List<String> findOrtByPlz(Integer plz);

    Integer findPlzByOrt(String ort);
}
