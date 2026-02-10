package com.ibosng.natifservice.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface NatifService {
    ResponseEntity<?> sendDocumentToNatif(MultipartFile file, Integer teilnehmerId);
}
