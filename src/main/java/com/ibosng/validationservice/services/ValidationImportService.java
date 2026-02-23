package com.ibosng.validationservice.services;

public interface ValidationImportService {

    void importUEBASeminars();

    void importFutureAbwesenheiten();

    void replaceIbosRefenceWithBenutzer();

    void importDataFromIbos();
}
