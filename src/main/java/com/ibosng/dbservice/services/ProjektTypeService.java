package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.ProjektType;

public interface ProjektTypeService extends BaseService<ProjektType> {
    // Add custom methods specific to Projekt entity
    ProjektType findByName(String name);
}
