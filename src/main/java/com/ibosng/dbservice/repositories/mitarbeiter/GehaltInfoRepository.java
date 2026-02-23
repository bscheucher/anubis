package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface GehaltInfoRepository extends JpaRepository<GehaltInfo, Integer> {

    GehaltInfo findByVertragsdaten_Id(Integer id);
}
