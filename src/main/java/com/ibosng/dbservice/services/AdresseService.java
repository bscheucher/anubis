package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.BasePlz;

import java.util.Optional;

public interface AdresseService extends BaseService<Adresse> {
    Optional<Adresse> findByPlzAndOrtAndStrasse(BasePlz plz, String ort, String strasse);
}
