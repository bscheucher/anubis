package com.ibosng.dbservice.services.jasper;

import com.ibosng.dbservice.entities.reports.DienstvertragDataDto;

import java.util.List;

public interface DienstvertragDataService {

    List<DienstvertragDataDto> getDvTestData(String personalnummer);
}
