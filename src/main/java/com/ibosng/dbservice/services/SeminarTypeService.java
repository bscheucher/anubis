package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.seminar.SeminarType;

public interface SeminarTypeService extends BaseService<SeminarType> {
    // Add custom methods specific to Seminar entity
    SeminarType findByName(String name);
}
