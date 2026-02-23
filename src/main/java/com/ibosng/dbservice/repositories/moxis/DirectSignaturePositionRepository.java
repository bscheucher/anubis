package com.ibosng.dbservice.repositories.moxis;

import com.ibosng.dbservice.entities.moxis.DirectSignaturePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface DirectSignaturePositionRepository extends JpaRepository<DirectSignaturePosition, Integer> {
}
