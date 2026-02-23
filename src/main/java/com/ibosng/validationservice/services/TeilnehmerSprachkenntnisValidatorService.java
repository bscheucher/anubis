package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.SprachkenntnisDto;

public interface TeilnehmerSprachkenntnisValidatorService {

    SprachkenntnisDto validateAndSaveSprachkenntnis(SprachkenntnisDto sprachkenntnisDto, String teilnehmerId, String changedBy);
}
