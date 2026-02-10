package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.ZusatzInfo;
import com.ibosng.dbservice.repositories.ZusatzInfoRepository;
import com.ibosng.dbservice.services.ZusatzInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZusatzInfoServiceImpl implements ZusatzInfoService {
    private final ZusatzInfoRepository zusatzInfoRepository;

    @Override
    public List<ZusatzInfo> findAll() {
        return zusatzInfoRepository.findAll();
    }

    @Override
    public Optional<ZusatzInfo> findById(Integer id) {
        return zusatzInfoRepository.findById(id);
    }

    @Override
    public ZusatzInfo save(ZusatzInfo object) {
        return zusatzInfoRepository.save(object);
    }

    @Override
    public List<ZusatzInfo> saveAll(List<ZusatzInfo> objects) {
        return zusatzInfoRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        zusatzInfoRepository.deleteById(id);
    }

    @Override
    public List<ZusatzInfo> findAllByIdentifier(String identifier) {
        return List.of();
    }


}
