package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.lhr.LhrJob;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;

import java.util.List;

public interface LHRUpdateJobsService {
    void executeLhrJobs(List<LhrJob> jobs);
    void executeZeiterfassungTransfers(List<ZeiterfassungTransfer> jobs);
}
