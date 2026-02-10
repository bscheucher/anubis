package com.ibosng.moxisservice.services;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface MoxisDocumentService {

    ResponseEntity<Resource> getAndUploadDocument(String processInstanceId, String personalnummer);
    ResponseEntity<Resource> getAndUploadVereinbarung(String processInstanceId, String personalnummer);
}
