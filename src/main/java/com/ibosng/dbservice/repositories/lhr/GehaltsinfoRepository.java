package com.ibosng.dbservice.repositories.lhr;

import com.ibosng.dbservice.entities.lhr.Gehaltsinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface GehaltsinfoRepository extends JpaRepository<Gehaltsinfo, Integer> {
}
