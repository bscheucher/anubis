package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.DataReferenceTemp;
import com.ibosng.dbservice.repositories.DataReferenceTempRepository;
import com.ibosng.dbservice.repositories.DataReferenceTempSpecification;
import com.ibosng.dbservice.services.DataReferenceTempService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataReferenceTempServiceImpl implements DataReferenceTempService {

    private final DataReferenceTempRepository dataReferenceTempRepository;

    @Override
    public List<DataReferenceTemp> findAll() {
        return dataReferenceTempRepository.findAll();
    }

    @Override
    public Optional<DataReferenceTemp> findById(Integer id) {
        return dataReferenceTempRepository.findById(id);
    }

    @Override
    public DataReferenceTemp save(DataReferenceTemp object) {
        return dataReferenceTempRepository.save(object);
    }


    @Override
    public List<DataReferenceTemp> saveAll(List<DataReferenceTemp> objects) {
        return dataReferenceTempRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        dataReferenceTempRepository.deleteById(id);
    }

    @Override
    public List<DataReferenceTemp> findAllByIdentifier(String identifier) {
        return dataReferenceTempRepository.findAll();
    }

    @Override
    public List<DataReferenceTemp> findAllByReference(String reference) {
        return dataReferenceTempRepository.findAllByReference(reference);
    }

    @Override
    public List<DataReferenceTemp> search(Map<String, String> filters) {
        Specification<DataReferenceTemp> spec = DataReferenceTempSpecification.buildDynamicSpec(filters);
        return dataReferenceTempRepository.findAll(spec);
    }
}
