package com.ibosng.lhrservice.services;

public interface SchedulerService {
    void closeMonaten();

    void syncLhrDocuments();

    void syncMAAbwesenheitenData();

    void resyncLeistungserfassungData();
}
