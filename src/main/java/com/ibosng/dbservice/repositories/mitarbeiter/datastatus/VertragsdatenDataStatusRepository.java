package com.ibosng.dbservice.repositories.mitarbeiter.datastatus;


import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VertragsdatenDataStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface VertragsdatenDataStatusRepository extends JpaRepository<VertragsdatenDataStatus, Integer> {
}
