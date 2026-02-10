package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.VereinbarungDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import com.ibosng.dbservice.repositories.vereinbarung.VereinbarungRepository;
import com.ibosng.dbservice.repositories.vereinbarung.VereinbarungSpecification;
import com.ibosng.dbservice.services.VereinbarungService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Mappers.mapVereinbarungToDto;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class VereinbarungServiceImpl implements VereinbarungService {

    private final VereinbarungRepository vereinbarungRepository;
    private final StammdatenService stammdatenService;



    @Override
    public Page<VereinbarungDto> findVereinbarungByCriteria(List<VereinbarungStatus> vereinbarungStatuses, String firmaName, String searchTerm, String sortBy, Sort.Direction direction, int page, int size) {
        Specification<Vereinbarung> spec = VereinbarungSpecification.filterVereinbarung(firmaName, searchTerm, vereinbarungStatuses);

        Sort sort;

        if (isNullOrBlank(sortBy) || direction == null) {
            // Default sorting order
            sort = Sort.by(
                    Sort.Order.desc("gueltigAb"),
//                    Sort.Order.asc("stammdaten.nachname"),
                    Sort.Order.asc("vereinbarungName")
            );
        } else {
            // Custom sorting based on parameters
            sort = Sort.by(direction, sortBy);
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Vereinbarung> vereinbarungenPage = vereinbarungRepository.findAll(spec, pageable);
        List<VereinbarungDto> vereinbarungDtos = new ArrayList<>();
        vereinbarungenPage.stream().forEach(vereinbarung -> {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(vereinbarung.getPersonalnummer().getPersonalnummer());
            if(stammdaten == null){
                log.error("No stammdaten found for: " + vereinbarung.toString());
            }
            vereinbarungDtos.add(mapVereinbarungToDto(vereinbarung, stammdaten));
        });
        return new PageImpl(vereinbarungDtos, pageable, vereinbarungenPage.getTotalElements());

    }

    @Override
    public Page<VereinbarungDto> getAllVereinbarungenPageable(int page, int size) {
        // TODO: add default sorting here?
       // Sort sort =
        Pageable pageable = PageRequest.of(page, size);
        List<VereinbarungDto> vereinbarungDtos = new ArrayList<>();
        Page<Vereinbarung> vereinbarungenPage = vereinbarungRepository.findAll(pageable);
        vereinbarungenPage.stream().forEach(vereinbarung -> {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(vereinbarung.getPersonalnummer().getPersonalnummer());
            if(stammdaten == null){
                log.error("No stammdaten found for: " + vereinbarung.toString());
            }
            vereinbarungDtos.add(mapVereinbarungToDto(vereinbarung, stammdaten));
        });

        return new PageImpl(vereinbarungDtos, pageable, vereinbarungenPage.getTotalElements());
    }

    @Override
    public Vereinbarung findVereinbarungByWorkflow_Id(Integer workflowId) {
        return vereinbarungRepository.findVereinbarungByWorkflow_Id(workflowId);
    }

    @Override
    public VereinbarungDto findVereinbarungById(Integer vereinbarungId) {
        Optional<Vereinbarung> vereinbarung = vereinbarungRepository.findById(vereinbarungId);
        if(vereinbarung.isEmpty()){
            log.error("No Vereinbarung found for id: " + vereinbarungId);
        }
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(vereinbarung.get().getPersonalnummer().getPersonalnummer());
        if(stammdaten == null){
            log.error("No stammdaten found for: " + vereinbarung.toString());
        }
        return mapVereinbarungToDto(vereinbarung.get(), stammdaten);
    }

    @Override
    public List<Vereinbarung> findAll() {
        return vereinbarungRepository.findAll();
    }

    @Override
    public Optional<Vereinbarung> findById(Integer id) {
        return vereinbarungRepository.findById(id);
    }

    @Override
    public Vereinbarung save(Vereinbarung object) {
        return vereinbarungRepository.save(object);
    }

    @Override
    public List<Vereinbarung> saveAll(List<Vereinbarung> objects) {
        return vereinbarungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        vereinbarungRepository.deleteById(id);
    }

    @Override
    public List<Vereinbarung> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Vereinbarung> findAllByPersonalnummer(Personalnummer personalnummer) {
        return vereinbarungRepository.findAllByPersonalnummer(personalnummer);
    }
}
