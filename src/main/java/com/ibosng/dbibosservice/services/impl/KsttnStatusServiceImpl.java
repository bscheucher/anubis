package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.ksttn.KsttnStatus;
import com.ibosng.dbibosservice.entities.teilnahme.Teilnahme;
import com.ibosng.dbibosservice.repositories.KsttnStatusRepository;
import com.ibosng.dbibosservice.services.KsttnStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KsttnStatusServiceImpl implements KsttnStatusService {
    private final KsttnStatusRepository ksttnStatusRepository;

    @Override
    public KsttnStatus findByTeilnahme(Teilnahme teilnahme) {
        return ksttnStatusRepository.findByKsttnkynrAndTeilnahmestatusBereichId(
                        teilnahme.getId().getAdresseAdnr(), teilnahme.getId().getSeminarSmnr(), teilnahme.getId().getDatum())
                .orElse(null);
    }

    @Override
    public List<KsttnStatus> findAll() {
        return ksttnStatusRepository.findAll();
    }

    @Override
    public Optional<KsttnStatus> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public KsttnStatus save(KsttnStatus object) {
        return ksttnStatusRepository.save(object);
    }

    @Override
    public List<KsttnStatus> saveAll(List<KsttnStatus> objects) {
        return ksttnStatusRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
    }

    @Override
    public KsttnStatus findAllByIdKsttnkynr(Integer id) {
        return ksttnStatusRepository.findAllByIdKsttnkynr(id);
    }
}
