package com.ibosng.dbservice.repositories.lhr;

import com.ibosng.dbservice.entities.lhr.Austrittsgrund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AustrittsgrundRepository extends JpaRepository<Austrittsgrund, Integer> {
    Austrittsgrund findAustrittsgrundByLhrGrund(String lhrGrund);
}
