package com.ibosng.moxisservice.services;

import com.ibosng.dbservice.entities.moxis.MoxisJob;

public interface MoxisUpdateJobsService {
    void updateActiveMoxisJobs();
    void updateActiveMoxisJobsSeparately();
    void processMoxisRetryJob();
    void updateJobAndWW(MoxisJob job);
}
