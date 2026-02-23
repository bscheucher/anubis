package com.ibosng.dbservice.services.impl.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Jobticket;
import com.ibosng.dbservice.repositories.masterdata.JobticketRepository;
import com.ibosng.dbservice.services.masterdata.JobticketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobticketServiceImpl implements JobticketService {

    private final JobticketRepository jobticketRepository;

    @Override
    public List<Jobticket> findAll() {
        return jobticketRepository.findAll();
    }

    @Override
    public Optional<Jobticket> findById(Integer id) {
        return jobticketRepository.findById(id);
    }

    @Override
    public Jobticket save(Jobticket object) {
        return jobticketRepository.save(object);
    }

    @Override
    public List<Jobticket> saveAll(List<Jobticket> objects) {
        return jobticketRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        jobticketRepository.deleteById(id);
    }

    @Override
    public List<Jobticket> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public Optional<Jobticket> findByName(String name) {
        return jobticketRepository.findByName(name);
    }

    @Override
    public List<Jobticket> findByStatus(Status status) {
        return jobticketRepository.findByStatus(status);
    }
}
