package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.TnZertifikatDto;

public interface TeilnehmerZertifikatValidatorService {

    TnZertifikatDto validateAndSaveZertifikat(TnZertifikatDto tnZertifikatDto, String changedBy, String teilnehmerId);
}
