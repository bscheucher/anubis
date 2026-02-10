package com.ibosng.dbservice.services.impl.lhr;

import com.ibosng.dbservice.entities.lhr.LhrJob;
import com.ibosng.dbservice.entities.lhr.LhrJobStatus;
import com.ibosng.dbservice.repositories.lhr.LhrJobRepository;
import com.ibosng.dbservice.services.lhr.LhrJobService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LhrJobServiceImpl implements LhrJobService {

    private final LhrJobRepository lhrJobRepository;

    public LhrJobServiceImpl(LhrJobRepository lhrJobRepository) {
        this.lhrJobRepository = lhrJobRepository;
    }

    @Override
    public List<LhrJob> findAll() {
        return lhrJobRepository.findAll();
    }

    @Override
    public Optional<LhrJob> findById(Integer id) {
        return lhrJobRepository.findById(id);
    }

    @Override
    public LhrJob save(LhrJob object) {
        return lhrJobRepository.save(object);
    }

    @Override
    public List<LhrJob> saveAll(List<LhrJob> objects) {
        return lhrJobRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        lhrJobRepository.deleteById(id);
    }

    @Override
    public List<LhrJob> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<LhrJob> findAllByStatusAndEintrittBefore(LhrJobStatus status, LocalDate date) {
        return lhrJobRepository.findAllByStatusAndEintrittBefore(status, date);
    }

    @Override
    public List<LhrJob> findAllByStatusAndEintrittLessThanEqual(LhrJobStatus status, LocalDate date) {
        return lhrJobRepository.findAllByStatusAndEintrittLessThanEqual(status, date);
    }

    @Override
    public List<LhrJob> findAllByStatusAndEintrittBetween(LhrJobStatus status, LocalDate startDate, LocalDate endDate) {
        return lhrJobRepository.findAllByStatusAndEintrittBetween(status, startDate, endDate);
    }
}
