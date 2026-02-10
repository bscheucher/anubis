package com.ibosng.moxisservice.clients;

import com.ibosng.dbservice.dtos.moxis.MoxisJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisReducedJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisUserDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.List;

public interface MoxisClient {
    ResponseEntity<String> startProcess(MultiValueMap<String, HttpEntity<?>> multiValueMap, boolean isExternalWithHandySignatur);

    ResponseEntity<String> cancelJob(String processInstanceId, MoxisUserDto user);

    ResponseEntity<MoxisJobStateDto> getJobState(String processInstanceId, String nameClassifier);

    ResponseEntity<List<MoxisReducedJobStateDto>> getJobStates(List<Integer> jobIds);

    Mono<File> getDocument(String processInstanceId);

    ResponseEntity<List<MoxisJobStateDto>> testConnection();
}
