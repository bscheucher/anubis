package com.ibosng.fileimportservice.controllers;

import com.ibosng.fileimportservice.services.impl.FileImportSchedulerServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Temporary controller to enable file import even when scheduling is disabled.
 */
@Slf4j
@RestController
@RequestMapping("/file-import")
@Tag(name = "File Import controller")
@RequiredArgsConstructor
public class FileImportController {

    private final FileImportSchedulerServiceImpl fileImportSchedulerService;

    @GetMapping("/trigger")
    public void triggerFileImport() {
        fileImportSchedulerService.checkIncomingFiles();
    }
}
