package com.ibosng.dbservice.repositories.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfoZulage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface GehaltInfoZulageRepository extends JpaRepository<GehaltInfoZulage, Integer> {

    List<GehaltInfoZulage> findAllByGehaltInfoId(Integer gehaltInfoId);


    Optional<GehaltInfoZulage> findByArtDerZulageAndGehaltInfoId(String artDerZulage, Integer gehaltInfoId);

    void deleteByArtDerZulageAndGehaltInfoId(String artDerZulage, Integer gehaltInfoId);
}
