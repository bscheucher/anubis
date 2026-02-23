package com.ibosng.aiservice.services.impl;

import com.ibosng.aiservice.services.AIFilesService;
import com.ibosng.aiservice.services.AISchedulerService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AISchedulerServiceImpl implements AISchedulerService {

    private final AIFilesService aiFilesService;
    private final FileShareService fileShareService;

    @Override
    public void updateChatSources() {
        List<File> files = fileShareService.downloadFilesToTemp("chat", "ai-sources");
        aiFilesService.updateFiles(files);
    }
}
