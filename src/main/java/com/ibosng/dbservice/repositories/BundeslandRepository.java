package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Bundesland;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface BundeslandRepository extends JpaRepository<Bundesland, Integer> {

    Bundesland findByPlzId(Integer plzId);

}
