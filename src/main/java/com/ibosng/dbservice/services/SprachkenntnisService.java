package com.ibosng.dbservice.services;

import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.entities.Sprachkenntnis;

import java.util.List;

public interface SprachkenntnisService extends BaseService<Sprachkenntnis> {
    List<Sprachkenntnis> findAllByTeilnehmerId(Integer teilnehmerId);

    SprachkenntnisDto mapTeilnehmerSprachkenntnisToDto(Sprachkenntnis sprachkenntnis);
}
