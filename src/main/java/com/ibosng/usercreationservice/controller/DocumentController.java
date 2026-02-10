package com.ibosng.usercreationservice.controller;

import com.ibosng.usercreationservice.service.UserCreationAnlageIbisAcamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.ibosng.usercreationservice.controller.DocumentController.MITARBEITER_URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(MITARBEITER_URI)
public class DocumentController {
    public static final String MITARBEITER_URI = "/mitarbeiter";
    public static final String UPLOAD_URI = "/upload";

    private final UserCreationAnlageIbisAcamService userAnlageIbisAcamService;

    @PostMapping(UPLOAD_URI)
    public ResponseEntity<Void> mitarbeiterToFile(@RequestParam String personnelnummer) {
        boolean success = userAnlageIbisAcamService.createMitarbeiterFile(personnelnummer);
        if (success) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
