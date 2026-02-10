package com.ibosng.dbservice.services.impl.mitarbeiter;


import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.repositories.mitarbeiter.PersonalnummerRepository;
import com.ibosng.dbservice.services.masterdata.IbisFirmaService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonalnummerServiceImpl implements PersonalnummerService {

    private final PersonalnummerRepository personalnummerRepository;

    private final IbisFirmaService ibisFirmaService;

    public PersonalnummerServiceImpl(PersonalnummerRepository personalnummerRepository,
                                     IbisFirmaService ibisFirmaService) {
        this.personalnummerRepository = personalnummerRepository;
        this.ibisFirmaService = ibisFirmaService;
    }

    @Override
    public List<Personalnummer> findAll() {
        return personalnummerRepository.findAll();
    }

    @Override
    public Optional<Personalnummer> findById(Integer id) {
        return personalnummerRepository.findById(id);
    }

    @Override
    public Personalnummer save(Personalnummer object) {
        return personalnummerRepository.save(object);
    }

    @Override
    public List<Personalnummer> saveAll(List<Personalnummer> objects) {
        return personalnummerRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        personalnummerRepository.deleteById(id);
    }

    @Override
    public List<Personalnummer> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Integer findMaxNummerByFirmaId(Integer firmaId) {
        Integer number = personalnummerRepository.findMaxNummerByFirmaId(firmaId);
        if(number != null) {
            return number;
        }
        return 0;
    }

    @Override
    public Personalnummer findByPersonalnummerAndBmdClient(String personalnummer, Integer bmdClient) {
        return personalnummerRepository.findByPersonalnummerAndFirma_BmdClient(personalnummer, bmdClient);
    }

    @Override
    public Personalnummer findByPersonalnummer(String personalnummer) {
        return personalnummerRepository.findByPersonalnummer(personalnummer);
    }

    @Override
    public Personalnummer generatePersonalNummer(String firmaName, MitarbeiterType mitarbeiterType, String createdBy) {
        IbisFirma ibisFirma = ibisFirmaService.findByName(firmaName);
        if(ibisFirma != null) {
            Personalnummer personalnummer = new Personalnummer();
            personalnummer.setFirma(ibisFirma);
            personalnummer.setMitarbeiterType(mitarbeiterType);
            personalnummer.setCreatedBy(createdBy);
            personalnummer.setStatus(Status.NEW);
            personalnummer.setIsIbosngOnboarded(true);
            String fourDigitsFirma = String.format("%04d", ibisFirma.getLhrNr());
            Integer number = findMaxNummerByFirmaId(ibisFirma.getId()) + 1;
            personalnummer.setNummer(number);
            String sixDigitsNumber = String.format("%06d", number);
            personalnummer.setPersonalnummer(fourDigitsFirma + sixDigitsNumber);
            return save(personalnummer);
        }
        return null;
    }

    @Override
    public void deleteAll(List<Personalnummer> personalnummerList) {
        personalnummerRepository.deleteAll(personalnummerList);
    }

    @Override
    public List<Personalnummer> findAllByMitarbeiterType(MitarbeiterType mitarbeiterType) {
        return personalnummerRepository.findAllByMitarbeiterType(mitarbeiterType);
    }

    @Override
    public List<Personalnummer> findAllByMitarbeiterTypeAndIsIbosngOnboarded(MitarbeiterType mitarbeiterType, Boolean isIbosngOnboarded) {
        return personalnummerRepository.findAllByMitarbeiterTypeAndIsIbosngOnboarded(mitarbeiterType, isIbosngOnboarded);
    }
}
