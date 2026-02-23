package com.ibosng.dbservice.services.zeitbuchung;

import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchungstyp;
import com.ibosng.dbservice.services.BaseService;

public interface ZeitbuchungstypService extends BaseService<Zeitbuchungstyp> {
    Zeitbuchungstyp findByType(String type);
}
