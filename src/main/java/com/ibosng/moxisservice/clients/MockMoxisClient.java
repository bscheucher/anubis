package com.ibosng.moxisservice.clients;

import com.ibosng.dbservice.dtos.moxis.MoxisJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisReducedJobStateDto;
import com.ibosng.dbservice.dtos.moxis.MoxisUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Mock implementation of {@link MoxisClient} returning hardcoded values.
 * Active only under the "development" Spring profile.
 */
@Service
@Slf4j
@Conditional(MoxisCondition.Reverse.class)
public class MockMoxisClient implements MoxisClient {

    @Override
    public ResponseEntity<String> startProcess(MultiValueMap<String, HttpEntity<?>> multiValueMap, boolean isExternalWithHandySignatur) {
        log.info("MOCK: startProcess called. isExternalWithHandySignatur={}, payloadParts={}",
                isExternalWithHandySignatur,
                multiValueMap != null ? multiValueMap.size() : 0);

        return ResponseEntity.ok("12345");
    }

    @Override
    public ResponseEntity<String> cancelJob(String processInstanceId, MoxisUserDto user) {
        log.info("MOCK: cancelJob called. processInstanceId={}, user={}", processInstanceId, user);

        String response = """
                {
                  "status": "cancelled",
                  "processInstanceId": "%s"
                }
                """.formatted(processInstanceId).strip();

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<MoxisJobStateDto> getJobState(String processInstanceId, String nameClassifier) {
        log.info("MOCK: getJobState called. processInstanceId={}, nameClassifier={}",
                processInstanceId, nameClassifier);

        MoxisJobStateDto dto = new MoxisJobStateDto();
        dto.setProcessInstanceId(processInstanceId);
        dto.setState("COMPLETED");
        dto.setProcessId("MOCK_PROCESS");
        dto.setCreationDate(OffsetDateTime.now().minusHours(1));
        dto.setEndDate(OffsetDateTime.now());
        dto.setCurrentIteration(1);
        dto.setIterations(Collections.emptyList());
        dto.setAdditionalRecipients(Collections.emptyList());

        MoxisUserDto owner = new MoxisUserDto();
        owner.setName("mock.user");
        owner.setClassifier(nameClassifier);
        dto.setOwner(owner);

        dto.setCustomMap(Map.of("mock", "true"));

        return ResponseEntity.ok(dto);
    }

    @Override
    public ResponseEntity<List<MoxisReducedJobStateDto>> getJobStates(List<Integer> jobIds) {
        log.info("MOCK: getJobStates called. jobIds={}", jobIds);

        List<MoxisReducedJobStateDto> list = new ArrayList<>();

        if (jobIds != null) {
            for (Integer id : jobIds) {
                MoxisReducedJobStateDto reduced = new MoxisReducedJobStateDto();
                reduced.setProcessInstanceId(String.valueOf(id));
                reduced.setState("COMPLETED");
                list.add(reduced);
            }
        }

        return ResponseEntity.ok(list);
    }

    @Override
    public Mono<File> getDocument(String processInstanceId) {
        log.info("MOCK: getDocument called. processInstanceId={}", processInstanceId);
    
        return Mono.fromCallable(() -> {
            try {
                File tempFile = File.createTempFile("mock-document-" + processInstanceId + "-", ".pdf");
                Files.writeString(tempFile.toPath(), "MOCK PDF CONTENT");
                return tempFile;
            } catch (IOException e) {
                log.error("MOCK: Failed to create temp file: {}", e.getMessage());
                return null;
            }
        })
        .subscribeOn(Schedulers.boundedElastic())  // Execute on thread pool for blocking operations
        .flatMap(file -> file == null ? Mono.empty() : Mono.just(file));
    }

    /**
     * Returns NOT_IMPLEMENTED as this method should only be invoked when not using the mock.
     */
    @Override
    public ResponseEntity<List<MoxisJobStateDto>> testConnection() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}