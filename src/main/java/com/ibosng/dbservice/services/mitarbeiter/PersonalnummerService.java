package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface PersonalnummerService extends BaseService<Personalnummer> {

    Integer findMaxNummerByFirmaId(Integer firmaId);

    Personalnummer findByPersonalnummerAndBmdClient(String personalnummer, Integer bmdClient);

    @Deprecated(since = "Use findByPersonalnummerAndBmdClient instead")
    Personalnummer findByPersonalnummer(String personalnummer);

    Personalnummer generatePersonalNummer(String firmaName, MitarbeiterType mitarbeiterType, String createdBy);

    void deleteAll(List<Personalnummer> personalnummerList);

    List<Personalnummer> findAllByMitarbeiterType(MitarbeiterType mitarbeiterType);

    List<Personalnummer> findAllByMitarbeiterTypeAndIsIbosngOnboarded(MitarbeiterType mitarbeiterType, Boolean isIbosngOnboarded);
}
