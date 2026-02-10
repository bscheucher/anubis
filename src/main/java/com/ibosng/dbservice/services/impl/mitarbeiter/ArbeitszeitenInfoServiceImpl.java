package com.ibosng.dbservice.services.impl.mitarbeiter;


import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.repositories.mitarbeiter.ArbeitszeitenInfoRepository;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArbeitszeitenInfoServiceImpl implements ArbeitszeitenInfoService {

    private final ArbeitszeitenInfoRepository arbeitszeitenInfoRepository;

    @Override
    public List<ArbeitszeitenInfo> findAll() {
        return arbeitszeitenInfoRepository.findAll();
    }

    @Override
    public Optional<ArbeitszeitenInfo> findById(Integer id) {
        return arbeitszeitenInfoRepository.findById(id);
    }

    @Override
    public ArbeitszeitenInfo save(ArbeitszeitenInfo object) {
        return arbeitszeitenInfoRepository.save(object);
    }

    @Override
    public List<ArbeitszeitenInfo> saveAll(List<ArbeitszeitenInfo> objects) {
        return arbeitszeitenInfoRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitszeitenInfoRepository.deleteById(id);
    }

    @Override
    public List<ArbeitszeitenInfo> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public ArbeitszeitenInfo findByVertragsdatenId(Integer vertragsdatenId) {
        return arbeitszeitenInfoRepository.findByVertragsdatenId(vertragsdatenId);
    }

    @Override
    public ArbeitszeitenInfo createNewArbeitszeitInfo(Vertragsdaten vertragsdaten, String createdBy) {
        ArbeitszeitenInfo arbeitszeitenInfo = new ArbeitszeitenInfo();
        arbeitszeitenInfo.setVertragsdaten(vertragsdaten);
        arbeitszeitenInfo.setCreatedBy(createdBy);
        arbeitszeitenInfo.setCreatedOn(getLocalDateNow());
        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NEW);
        log.info("Creating arbeitszeitenInfo during MitarbeiterArbeitszeitenInfoValidation");
        return save(arbeitszeitenInfo);
    }
}
