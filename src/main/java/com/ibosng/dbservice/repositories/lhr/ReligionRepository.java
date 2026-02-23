package com.ibosng.dbservice.repositories.lhr;

import com.ibosng.dbservice.entities.lhr.Religion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface ReligionRepository extends JpaRepository<Religion, Integer> {

    Optional<Religion> findByName(String name);
}
