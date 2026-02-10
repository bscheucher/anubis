package com.ibosng.dbservice.services.impl.mitarbeiter;


import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VordienstzeitenDataStatus;
import com.ibosng.dbservice.repositories.mitarbeiter.VordienstzeitenRepository;
import com.ibosng.dbservice.services.mitarbeiter.VordienstzeitenService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
public class VordienstzeitenServiceImpl implements VordienstzeitenService {

    private final VordienstzeitenRepository vordienstzeitenRepository;

    public VordienstzeitenServiceImpl(VordienstzeitenRepository vordienstzeitenRepository) {
        this.vordienstzeitenRepository = vordienstzeitenRepository;
    }

    @Override
    public List<Vordienstzeiten> findAll() {
        return vordienstzeitenRepository.findAll();
    }

    @Override
    public Optional<Vordienstzeiten> findById(Integer id) {
        return vordienstzeitenRepository.findById(id);
    }

    @Override
    public Vordienstzeiten save(Vordienstzeiten object) {
        return vordienstzeitenRepository.save(object);
    }

    @Override
    public List<Vordienstzeiten> saveAll(List<Vordienstzeiten> objects) {
        return vordienstzeitenRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        vordienstzeitenRepository.deleteById(id);
    }

    @Override
    public List<Vordienstzeiten> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Optional<Vordienstzeiten> findByPersonalnummer(String personalnummer) {
        return vordienstzeitenRepository.findByPersonalnummer(personalnummer);
    }

    @Override
    public List<Vordienstzeiten> findAllByVertragsdatenId(Integer vertragsdatenId) {
        return vordienstzeitenRepository.findAllByVertragsdatenId(vertragsdatenId);
    }

    @Override
    public VordienstzeitenDto mapVordienstzeitenToDto(Vordienstzeiten vordienstzeiten) {
        VordienstzeitenDto vordienstzeitenDto = new VordienstzeitenDto();
        if (vordienstzeiten.getId() != null) {
            vordienstzeitenDto.setId(vordienstzeiten.getId());
        }
        if (vordienstzeiten.getVertragsdaten() != null &&
                vordienstzeiten.getVertragsdaten().getPersonalnummer() != null &&
                !isNullOrBlank(vordienstzeiten.getVertragsdaten().getPersonalnummer().getPersonalnummer())
        ) {
            vordienstzeitenDto.setPersonalnummer(vordienstzeiten.getVertragsdaten().getPersonalnummer().getPersonalnummer());
        }
        if (vordienstzeiten.getVertragsart() != null) {
            vordienstzeitenDto.setVertragsart(vordienstzeiten.getVertragsart().getName());
        }
        if (vordienstzeiten.getFirma() != null) {
            vordienstzeitenDto.setFirma(vordienstzeiten.getFirma());
        }
        if (vordienstzeiten.getVon() != null) {
            vordienstzeitenDto.setVordienstzeitenVon(vordienstzeiten.getVon().toString());
        }
        if (vordienstzeiten.getBis() != null) {
            vordienstzeitenDto.setVordienstzeitenBis(vordienstzeiten.getBis().toString());
        }
        if (vordienstzeiten.getWochenstunden() != null) {
            vordienstzeitenDto.setVWochenstunden(String.valueOf(vordienstzeiten.getWochenstunden()));
        }
        vordienstzeitenDto.setAnrechenbar(vordienstzeiten.isAnrechenbar());
        if (!isNullOrBlank(vordienstzeiten.getNachweisFileName())) {
            vordienstzeitenDto.setNachweisFilename(vordienstzeiten.getNachweisFileName());
        }
        if (vordienstzeiten.getNachweisStatus() != null) {
            vordienstzeitenDto.setNachweis(vordienstzeiten.getNachweisStatus().getValue());
        }
        if (vordienstzeiten.getFacheinschlaegig() != null) {
            vordienstzeitenDto.setFacheinschlaegig(vordienstzeiten.getFacheinschlaegig());
        }
        for (VordienstzeitenDataStatus dataStatus : vordienstzeiten.getErrors()) {
            vordienstzeitenDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
        }
        vordienstzeitenDto.setErrors(new ArrayList<>(vordienstzeitenDto.getErrorsMap().keySet()));
        return vordienstzeitenDto;
    }

    @Override
    public List<Vordienstzeiten> findAllByCreatedOnOrChangedOnAfter(LocalDateTime after) {
        return vordienstzeitenRepository.findAllByCreatedOnAfterOrChangedOnAfter(after, after);
    }
}
