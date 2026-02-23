package com.ibosng.natifservice.services.impl;

import com.ibosng.natifservice.client.NatifClient;
import com.ibosng.natifservice.services.NatifService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class NatifServiceImpl implements NatifService {

    private final NatifClient natifClient;

    @Override
    public ResponseEntity<?> sendDocumentToNatif(MultipartFile file, Integer teilnehmerId) {

        return natifClient.getExtractionResults(file, teilnehmerId);
    }
}
