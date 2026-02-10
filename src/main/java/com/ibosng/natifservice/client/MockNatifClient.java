package com.ibosng.natifservice.client;

import com.ibosng.natifservice.dtos.extractions.ExtractionsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Mock implementation of the {@link NatifClient} interface intended for use in a development profile.
 * <p>
 * This class simulates the behavior of a real client by providing a mock implementation
 * for the {@code getExtractionResults} method. It does not perform any actual external calls
 * or processes but can be modified to return mock data if necessary.
 * <p>
 */
@Service
@Slf4j
@Profile({"localdev", "test"})
public class MockNatifClient implements NatifClient {

    @Override
    public ResponseEntity<ExtractionsDto> getExtractionResults(MultipartFile file, Integer teilnehmerId) {
        log.info("MOCK: Simulating extraction results for file: {} and teilnehmerId: {}",
                file != null ? file.getOriginalFilename() : "null", teilnehmerId);

        // Can be customized to return mock data if needed
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
