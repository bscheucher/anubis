package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.LvAcceptanceDto;
import com.ibosng.dbservice.entities.mitarbeiter.LvAcceptance;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface LvAcceptanceService extends BaseService<LvAcceptance> {
    List<LvAcceptance> findByPersonalnummer(String personalnummerString);
    LvAcceptanceDto mapLvAcceptanceToDto(LvAcceptance lvAcceptance);
    LvAcceptance mapLvAcceptanceDtoToEntity(LvAcceptanceDto lvAcceptanceDto, LvAcceptance lvAcceptance);

}
