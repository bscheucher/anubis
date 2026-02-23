package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.FileImportHeaders;
import com.ibosng.dbservice.entities.FileType;
import com.ibosng.dbservice.repositories.HeaderRepository;
import com.ibosng.dbservice.services.HeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.parseCsvString;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeaderServiceImpl implements HeaderService {

    private final HeaderRepository headerRepository;

    @Override
    public List<FileImportHeaders> findAll() {
        return headerRepository.findAll();
    }

    @Override
    public Optional<FileImportHeaders> findById(Integer id) {
        return headerRepository.findById(id);
    }

    @Override
    public FileImportHeaders save(FileImportHeaders object) {
        return headerRepository.save(object);
    }

    @Override
    public List<String> getActiveHeadersNamesByFileType(FileType fileType) {
        Optional<FileImportHeaders> optionalHeader = headerRepository.findByFileType(fileType);
        optionalHeader.ifPresent(header -> log.debug("Found header for file type [{}]: {}", fileType, header));
        return optionalHeader
                .map(header -> parseCsvString(header.getActiveHeaders()))
                .orElse(new ArrayList<>());
    }

    @Override
    public List<String> getInactiveHeadersNamesByFileType(FileType fileType) {
        Optional<FileImportHeaders> optionalHeader = headerRepository.findByFileType(fileType);
        optionalHeader.ifPresent(header -> log.debug("Found header for file type [{}]: {}", fileType, header));
        return optionalHeader
                .map(header -> parseCsvString(header.getInactiveHeaders()))
                .orElse(new ArrayList<>());
    }

    @Override
    public FileImportHeaders findByFileType(FileType fileType) {
        return headerRepository.findByFileType(fileType).orElseThrow(() ->
                new RuntimeException("No header for fileType %s was found".formatted(fileType.name())));
    }

    @Override
    public List<FileImportHeaders> saveAll(List<FileImportHeaders> objects) {
        return headerRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        headerRepository.deleteById(id);
    }

    @Override
    public List<FileImportHeaders> findAllByIdentifier(String identifier) {
        return headerRepository.findAll();
    }
}
