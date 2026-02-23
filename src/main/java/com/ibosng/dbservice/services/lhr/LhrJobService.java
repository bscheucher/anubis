package com.ibosng.dbservice.services.lhr;

import com.ibosng.dbservice.entities.lhr.LhrJob;
import com.ibosng.dbservice.entities.lhr.LhrJobStatus;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDate;
import java.util.List;

public interface LhrJobService extends BaseService<LhrJob> {
    List<LhrJob> findAllByStatusAndEintrittBefore(LhrJobStatus status, LocalDate date);
    List<LhrJob> findAllByStatusAndEintrittLessThanEqual(LhrJobStatus status, LocalDate date);
    List<LhrJob> findAllByStatusAndEintrittBetween(LhrJobStatus status, LocalDate startDate, LocalDate endDate);
}
