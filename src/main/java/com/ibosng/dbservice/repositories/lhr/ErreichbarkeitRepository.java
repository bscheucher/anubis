package com.ibosng.dbservice.repositories.lhr;

import com.ibosng.dbservice.entities.lhr.Erreichbarkeit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface ErreichbarkeitRepository extends JpaRepository<Erreichbarkeit, Integer> {
    List<Erreichbarkeit> findAllByErreichbarkeitsart(String erreichbarkeitsart);
    List<Erreichbarkeit> findAllByKz(String kz);
}
