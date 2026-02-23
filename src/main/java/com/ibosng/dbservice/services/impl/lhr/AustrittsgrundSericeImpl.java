package com.ibosng.dbservice.services.impl.lhr;

import com.ibosng.dbservice.entities.lhr.Austrittsgrund;
import com.ibosng.dbservice.repositories.lhr.AustrittsgrundRepository;
import com.ibosng.dbservice.services.lhr.AustrittsgrundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AustrittsgrundSericeImpl implements AustrittsgrundService {

    private final AustrittsgrundRepository austrittsgrundRepository;

    @Override
    public List<Austrittsgrund> findAll() {
        return austrittsgrundRepository.findAll();
    }

    @Override
    public Optional<Austrittsgrund> findById(Integer id) {
        return austrittsgrundRepository.findById(id);
    }

    @Override
    public Austrittsgrund save(Austrittsgrund object) {
        return austrittsgrundRepository.save(object);
    }

    @Override
    public List<Austrittsgrund> saveAll(List<Austrittsgrund> objects) {
        return austrittsgrundRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        austrittsgrundRepository.deleteById(id);
    }

    @Override
    public List<Austrittsgrund> findAllByIdentifier(String identifier) {
        return List.of();
    }


    @Override
    public Austrittsgrund findAustrittsgrundByLhrGrund(String lhrGrund) {
        return austrittsgrundRepository.findAustrittsgrundByLhrGrund(lhrGrund);
    }
}
