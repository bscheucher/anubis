package com.ibosng.dbservice.services.impl.lhr;

import com.ibosng.dbservice.dtos.teilnehmer.AbmeldungDto;
import com.ibosng.dbservice.entities.lhr.Abmeldung;
import com.ibosng.dbservice.entities.lhr.AbmeldungStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.repositories.lhr.AbmeldungRepository;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.lhr.AbmeldungService;
import com.ibosng.dbservice.services.lhr.AustrittsgrundService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.*;

@RequiredArgsConstructor
@Service
public class AbmeldungServiceImpl implements AbmeldungService {

    private final AbmeldungRepository abmeldungRepository;

    private final PersonalnummerService personalnummerService;

    private final AustrittsgrundService austrittsgrundService;

    private final TeilnehmerService teilnehmerService;

    private final VertragsdatenService vertragsdatenService;

    @Override
    public List<Abmeldung> findAll() {
        return abmeldungRepository.findAll();
    }

    @Override
    public Optional<Abmeldung> findById(Integer id) {
        return abmeldungRepository.findById(id);
    }

    @Override
    public Abmeldung save(Abmeldung object) {
        return abmeldungRepository.save(object);
    }

    @Override
    public List<Abmeldung> saveAll(List<Abmeldung> objects) {
        return abmeldungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        abmeldungRepository.deleteById(id);
    }

    @Override
    public List<Abmeldung> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public Page<AbmeldungDto> findAll(Pageable pageable) {
        return abmeldungRepository.findAll(pageable).map(this::mapToAbmeldungDto);
    }

    @Override
    public AbmeldungDto findByTeilnehmerId(Integer teilnehmerId) {
        Optional<Teilnehmer> teilnehmerOptional = teilnehmerService.findById(teilnehmerId);
        return teilnehmerOptional.flatMap(teilnehmer -> {
            Abmeldung abmeldung = this.abmeldungRepository.findByPersonalnummerId(teilnehmer.getPersonalnummer().getId());
            if (abmeldung != null) {
                return Optional.of(this.mapToAbmeldungDto(abmeldung));
            } else {
                return Optional.empty();
            }
        }).orElse(null);
    }

    @Override
    public AbmeldungDto mapToAbmeldungDto(Abmeldung abmeldung) {

        AbmeldungDto abmeldungDto = AbmeldungDto.builder()
                .id(abmeldung.getId())
                .austrittsDatum(String.valueOf(abmeldung.getAustrittsDatum()))
                .bemerkung(abmeldung.getBemerkung())
                .svNummer(abmeldung.getSvNummer())
                .status(abmeldung.getStatus().name())
                .build();

        if (abmeldung.getPersonalnummer() != null) {
            Teilnehmer pnTn = teilnehmerService.findByPersonalnummerId(abmeldung.getPersonalnummer().getId());
            if (pnTn != null) {
                if (!isNullOrBlank(pnTn.getVorname())) {
                    abmeldungDto.setVorname(pnTn.getVorname());
                }
                if (!isNullOrBlank(pnTn.getNachname())) {
                    abmeldungDto.setNachname(pnTn.getNachname());
                }
                if (pnTn.getId() != null) {
                    abmeldungDto.setTeilnehmerId(pnTn.getId());
                }
                Vertragsdaten vertragsdaten = vertragsdatenService.findByPersonalnummerStringAndStatus(pnTn.getPersonalnummer().getPersonalnummer(), List.of(MitarbeiterStatus.ACTIVE)).stream().findAny().orElse(null);
                if (vertragsdaten != null && vertragsdaten.getKostenstelle() != null) {
                    abmeldungDto.setKostenstelle(vertragsdaten.getKostenstelle().getBezeichnung());
                }
            }
            if (!isNullOrBlank(abmeldung.getBemerkung())) {
                abmeldungDto.setBemerkung(abmeldung.getBemerkung());
            }
        }

        if (abmeldung.getAustrittsgrund() != null) {
            abmeldungDto.setAustrittsgrund(abmeldung.getAustrittsgrund().getLhrGrund());
        }

        return abmeldungDto;
    }

    @Override
    public Abmeldung mapToAbmeldung(AbmeldungDto abmeldungDto, AbmeldungStatus abmeldungStatus) {
        Abmeldung abmeldung = new Abmeldung();
        if (abmeldungDto.getId() != null) {
            abmeldung.setId(abmeldungDto.getId());
        }
        Optional<Teilnehmer> teilnehmer = teilnehmerService.findById(abmeldungDto.getTeilnehmerId());
        if (teilnehmer.isPresent()) {
            if (teilnehmer.get().getPersonalnummer() != null) {
                abmeldung.setPersonalnummer(teilnehmer.get().getPersonalnummer());
            }
            if (teilnehmer.get().getSvNummer() != null) {
                abmeldung.setSvNummer(teilnehmer.get().getSvNummer());
            }
        }
        if (isValidDate(abmeldungDto.getAustrittsDatum())) {
            abmeldung.setAustrittsDatum(parseDate(abmeldungDto.getAustrittsDatum()));
        }
        if (!isNullOrBlank(abmeldungDto.getBemerkung())) {
            abmeldung.setBemerkung(abmeldungDto.getBemerkung());
        }
        if (!isNullOrBlank(abmeldungDto.getAustrittsgrund())) {
            abmeldung.setAustrittsgrund(austrittsgrundService.findAustrittsgrundByLhrGrund(abmeldungDto.getAustrittsgrund()));
        }
        abmeldung.setStatus(abmeldungStatus);
        return abmeldung;
    }

    @Override
    public Abmeldung findAllBySvNummer(String svNummer) {
        return abmeldungRepository.findAllBySvNummer(svNummer);
    }

    @Override
    public Optional<Abmeldung> findAllTeilnehmerID(Integer teilnehmerId) {
        Optional<Teilnehmer> teilnehmerOptional = teilnehmerService.findById(teilnehmerId);
        return teilnehmerOptional.flatMap(teilnehmer -> {
            Abmeldung abmeldung = this.abmeldungRepository.findByPersonalnummerId(teilnehmer.getPersonalnummer().getId());
            if (abmeldung != null) {
                return Optional.of(abmeldung);
            } else {
                return Optional.empty();
            }
        });
    }

}
