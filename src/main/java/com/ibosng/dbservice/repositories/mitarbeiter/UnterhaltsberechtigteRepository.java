package com.ibosng.dbservice.repositories.mitarbeiter;


import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface UnterhaltsberechtigteRepository extends JpaRepository<Unterhaltsberechtigte, Integer> {
    List<Unterhaltsberechtigte> findAllByVertragsdatenId(Integer vertragsdatenId);

    List<Unterhaltsberechtigte> findAllByCreatedOnAfterOrChangedOnAfter(LocalDateTime createdOn, LocalDateTime changedOn);
}
