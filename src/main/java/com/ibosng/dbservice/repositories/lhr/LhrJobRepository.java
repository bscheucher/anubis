package com.ibosng.dbservice.repositories.lhr;

import com.ibosng.dbservice.entities.lhr.LhrJob;
import com.ibosng.dbservice.entities.lhr.LhrJobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface LhrJobRepository extends JpaRepository<LhrJob, Integer> {

    List<LhrJob> findAllByStatusAndEintrittBefore(LhrJobStatus status, LocalDate date);
    List<LhrJob> findAllByStatusAndEintrittLessThanEqual(LhrJobStatus status, LocalDate date);

    List<LhrJob> findAllByStatusAndEintrittBetween(LhrJobStatus status, LocalDate startDate, LocalDate endDate);
}
