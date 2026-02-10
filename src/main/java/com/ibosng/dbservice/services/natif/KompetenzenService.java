package com.ibosng.dbservice.services.natif;

import com.ibosng.dbservice.entities.natif.Kompetenz;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface KompetenzenService extends BaseService<Kompetenz> {

    List<Kompetenz> getKompetenzByTeilnehmerId(Integer teilnehmerId);

    void deleteByTeilnehmerId(Integer teilnehmerId);

    boolean isExists(Kompetenz kompetenz);
}
