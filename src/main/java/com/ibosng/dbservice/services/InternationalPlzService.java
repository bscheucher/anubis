package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.InternationalPlz;

import java.util.List;
import java.util.Optional;

public interface InternationalPlzService extends BaseService<InternationalPlz> {
    List<String> getAllPlz();

    List<String> getAllOrt();

    List<InternationalPlz> findByPlz(String plz);

    List<String> findOrtByPlz(String plz);

    String findPlzByOrt(String ort);

    Optional<InternationalPlz> findPlzByPlzOrtLand(String plz, String ort, Integer land);
}
