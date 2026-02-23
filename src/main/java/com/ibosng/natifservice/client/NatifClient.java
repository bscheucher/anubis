package com.ibosng.natifservice.client;

import com.ibosng.natifservice.dtos.extractions.ExtractionsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface NatifClient {
    ResponseEntity<ExtractionsDto> getExtractionResults(MultipartFile file, Integer teilnehmerId);
}
