package com.ibosng.dbservice.repositories.mitarbeiter.datastatus;


import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VordienstzeitenDataStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface VordienstzeitenDataStatusRepository extends JpaRepository<VordienstzeitenDataStatus, Integer> {
}
