package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfo;
import com.ibosng.dbservice.services.BaseService;

public interface GehaltInfoService extends BaseService<GehaltInfo> {
    GehaltInfo findByVertragsdatenId(Integer vertragsdatenId);
}
