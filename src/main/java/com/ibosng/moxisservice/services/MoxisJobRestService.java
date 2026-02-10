package com.ibosng.moxisservice.services;

import com.ibosng.dbservice.dtos.moxis.MoxisJobDto;
import com.ibosng.dbservice.dtos.moxis.MoxisJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisReducedJobStateDto;
import com.ibosng.dbservice.entities.moxis.MoxisJob;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MoxisJobRestService {

    ResponseEntity<String> startProcess(MoxisJobDto jobDto, MoxisJob job, boolean isRestCall);

    ResponseEntity<String> cancelJob(String personalnummer, String userEmail);

    ResponseEntity<MoxisJobStateDto> getJobState(String processInstanceId, String nameClassifier);

    ResponseEntity<List<MoxisReducedJobStateDto>> getStatesForJobs(List<Integer> jobIds);
}
