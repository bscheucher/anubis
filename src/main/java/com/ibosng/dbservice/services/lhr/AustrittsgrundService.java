package com.ibosng.dbservice.services.lhr;

import com.ibosng.dbservice.entities.lhr.Austrittsgrund;
import com.ibosng.dbservice.services.BaseService;

public interface AustrittsgrundService extends BaseService<Austrittsgrund> {
    Austrittsgrund findAustrittsgrundByLhrGrund(String lhrGrund);
}
