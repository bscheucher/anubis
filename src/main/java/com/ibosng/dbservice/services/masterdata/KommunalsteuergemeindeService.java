package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Kommunalsteuergemeinde;
import com.ibosng.dbservice.services.BaseService;

public interface KommunalsteuergemeindeService extends BaseService<Kommunalsteuergemeinde> {
    Kommunalsteuergemeinde findByDienstortPlz(Integer dienstortPlz);
}
