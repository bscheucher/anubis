package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.repositories.BenutzerRepository;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class BenutzerServiceImpl implements BenutzerService {

    private final BenutzerRepository benutzerRepository;
    private final StammdatenService stammdatenService;

    @Value("${isProduction:#{null}}")
    private String isProduction;

    private boolean isProduction() {
        return Boolean.parseBoolean(isProduction);
    }

    @Override
    public Optional<Benutzer> findById(Integer id) {
        return benutzerRepository.findById(id);
    }

    @Override
    public List<Benutzer> findAll() {
        return benutzerRepository.findAll();
    }

    @Override
    public Benutzer save(Benutzer benutzer) {
        return benutzerRepository.save(benutzer);
    }

    @Override
    public void deleteById(Integer id) {
        this.benutzerRepository.deleteById(id);
    }

    @Override
    public List<Benutzer> saveAll(List<Benutzer> benutzers) {
        return benutzerRepository.saveAll(benutzers);
    }

    @Override
    public List<Benutzer> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Benutzer getBenutzerByAzureId(String azureId) {
        List<Benutzer> benutzers = benutzerRepository.getBenutzerByAzureId(azureId);
        return findFirstObject(benutzers, new HashSet<>(Set.of(azureId)), "Benutzer");
    }

    @Override
    public Benutzer findByEmail(String email) {
        List<Benutzer> benutzers = benutzerRepository.findByEmailIgnoreCase(email);
        return findFirstObject(benutzers, new HashSet<>(Set.of(email)), "Benutzer");
    }

    @Override
    public Benutzer findAllByFirstNameAndLastName(String firstName, String lastName) {
        List<Benutzer> benutzers = benutzerRepository.findAllByFirstNameAndLastName(firstName, lastName);
        return findFirstObject(benutzers, new HashSet<>(Set.of(firstName, lastName)), "Benutzer");
    }

    @Override
    public Benutzer createUserIfNotExists(Integer stammdatenId, String workEmail, String createdBy) {
        Benutzer benutzer = null;
        Optional<Stammdaten> stammdatenOptional = stammdatenService.findById(stammdatenId);
        Stammdaten stammdaten = null;
        if (stammdatenOptional.isPresent()) {
            stammdaten = stammdatenOptional.get();
        }
        if (!isNullOrBlank(workEmail)) {
            workEmail = workEmail.toLowerCase();
            benutzer = findByEmail(workEmail);
        }
        if (benutzer == null && stammdaten != null) {
            benutzer = new Benutzer();
            benutzer.setStatus(Status.ACTIVE);
            benutzer.setCreatedBy(createdBy);
            if (!isNullOrBlank(workEmail)) {
                benutzer.setEmail(workEmail);
            }
            if (!isNullOrBlank(stammdaten.getVorname())) {
                benutzer.setFirstName(stammdaten.getVorname());
            }
            if (!isNullOrBlank(stammdaten.getNachname())) {
                benutzer.setLastName(stammdaten.getNachname());
            }
            benutzer = save(benutzer);
        } else if (benutzer != null) {
            benutzer.setChangedBy(createdBy);
            benutzer = save(benutzer);
        }
        return benutzer;
    }

    @Override
    public Benutzer getBenutzerByPersonalnummer(String personalnummer) {
        return benutzerRepository.findByPersonalnummer_Personalnummer(personalnummer);
    }

    @Override
    public Benutzer findByPersonalnummerAndFirmaBmdClient(String personalnummer, Integer firma) {
        return benutzerRepository.findFirstByPersonalnummer_PersonalnummerAndPersonalnummer_Firma_BmdClientOrderByCreatedOnDesc(personalnummer, firma).orElse(null);
    }

    @Override
    public List<Benutzer> findAllBySamIbosName(String samIbos) {
        return benutzerRepository.findAllBySamIbosName(samIbos);
    }

    @Override
    public Benutzer findByUpn(String upn) {
        if (!isProduction()) {
            return benutzerRepository.findByUpnContainingIgnoreCase(upn);
        }
        return benutzerRepository.findByUpnIgnoreCase(upn);
    }

    @Override
    public Benutzer findByUpnContaining(String upn) {
        return benutzerRepository.findByUpnContainingIgnoreCase(upn);
    }

    @Override
    public Benutzer findByAzureId(String azureId) {
        return benutzerRepository.findByAzureId(azureId);
    }

    @Override
    public Benutzer findByPersonalnummer(Personalnummer personalnummer) {
        List<Benutzer> benutzers = benutzerRepository.findByPersonalnummer(personalnummer);

        String personalnummerStr = String.valueOf(personalnummer.getPersonalnummer());
        String firmaName = personalnummer.getFirma() != null ? personalnummer.getFirma().getName() : "n/a";

        if (benutzers.size() > 1) {
            log.warn("Multiple Benutzer found for personalnummer: {} and firma: {}", personalnummerStr, firmaName);
            return null;
        }

        if (benutzers.isEmpty()) {
            log.warn("No Benutzer found for personalnummer: {} and firma: {}", personalnummerStr, firmaName);
            return null;
        }

        return benutzers.get(0);
    }

    @Override
    public Benutzer findByPersonalnummerId(Integer personalnummerId) {
        return benutzerRepository.findByPersonalnummer_Id(personalnummerId);
    }
}
