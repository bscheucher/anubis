package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.ArtDerZulage;
import com.ibosng.dbservice.services.BaseService;

public interface ArtDerZulageService extends BaseService<ArtDerZulage> {

    ArtDerZulage findByName(String name);
}
