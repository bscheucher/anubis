package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.Sprachkenntnis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface SprachkenntnisRepository extends JpaRepository<Sprachkenntnis, Integer> {
    List<Sprachkenntnis> findAllByTeilnehmerId(Integer teilnehmerId);
}
