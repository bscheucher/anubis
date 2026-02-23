package com.ibosng.dbservice.services.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungReason;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface ZeiterfassungReasonService extends BaseService<ZeiterfassungReason> {

    ZeiterfassungReason findByBezeichnungAndShortBezeichnung(String bezeichnung, String shortBezeichnung);
    ZeiterfassungReason findByBezeichnung(String bezeichnung);
    List<ZeiterfassungReason> findAllByIbosId(Integer ibosId);
    List<ZeiterfassungReason> findAllByIbosIdAndBezeichnung(Integer ibosId, String bezeichnung);
    List<ZeiterfassungReason> findAllByIbosIdAndShortBezeichnung(Integer ibosId, String shortBezeichnung);
    List<Integer> findAllReasonsWithIbosidNotNull();
}
