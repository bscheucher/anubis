package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface ArbeitszeitenInfoRepository extends JpaRepository<ArbeitszeitenInfo, Integer> {
    @Query("select ari from ArbeitszeitenInfo ari where ari.vertragsdaten.id = :vertragsdatenId")
    ArbeitszeitenInfo findByVertragsdatenId(Integer vertragsdatenId);
}
