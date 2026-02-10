package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.repositories.LandRepository;
import com.ibosng.dbservice.services.LandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;

@Service
@Slf4j
public class LandServiceImpl implements LandService {

    private final LandRepository landRepository;

    @Autowired
    public LandServiceImpl(LandRepository landRepository) {
        this.landRepository = landRepository;
    }

    @Override
    public Optional<Land> findById(Integer id) {
        return landRepository.findById(id);
    }

    @Override
    public List<Land> findAll() {
        return landRepository.findAll();
    }

    @Override
    public Land save(Land land) {
        return landRepository.save(land);
    }

    @Override
    public List<Land> saveAll(List<Land> lands) {
        return landRepository.saveAll(lands);
    }

    @Override
    public void deleteById(Integer id) {
        this.landRepository.deleteById(id);
    }

    @Override
    public List<Land> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Land> findByTelefonvorwahl(String vorwahl) {
        return landRepository.findByTelefonvorwahl(vorwahl);
    }

    @Override
    public Land findByEldaCode(String vorwahl) {
        return findFirstObject(landRepository.findAllByEldaCode(vorwahl), new HashSet<>(List.of(vorwahl)), "Land");
    }

    @Override
    public Land findByLandName(String landname) {
        return landRepository.findByLandName(landname);
    }

    @Override
    public List<Land> findByIsInEuEeaCh(Boolean isIn) {
        return landRepository.findByIsInEuEeaCh(isIn);
    }

    @Override
    public List<Land> findByLandCode(String landcode) {
        return landRepository.findByLandCode(landcode);
    }

    @Override
    public Land findAllByLhrKz(String lhrKz) {
        return findFirstObject(landRepository.findAllByLhrKz(lhrKz), new HashSet<>(List.of(lhrKz)), "Land");
    }

    @Override
    public Land getLandFromCountryCode(String countryCode) {
        List<Land> laender = findByTelefonvorwahl(countryCode);
        return findFirstObject(laender, new HashSet<>(Set.of(countryCode)), "Land");
    }
}
