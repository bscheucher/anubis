package com.ibosng.dbservice.services.lhr;

import com.ibosng.dbservice.entities.lhr.Religion;
import com.ibosng.dbservice.services.BaseService;

import java.util.Optional;

public interface ReligionService extends BaseService<Religion> {
    Optional<Religion> findByName(String name);
}
