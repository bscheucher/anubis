package com.ibosng.dbservice.services.impl.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.MAFilteredResultDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MASearchCriteriaDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MitarbeiterSummaryDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.workflows.WorkflowItemDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.StammdatenDataStatus;
import com.ibosng.dbservice.repositories.mitarbeiter.StammdatenRepository;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Helpers.getSvnForMitarbeiter;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Service
public class StammdatenServiceImpl implements StammdatenService {

    private final StammdatenRepository stammdatenRepository;
    private final PersonalnummerService personalnummerService;
    private final WWorkflowItemService wWorkflowItemService;

    public StammdatenServiceImpl(StammdatenRepository stammdatenRepository, PersonalnummerService personalnummerService, WWorkflowItemService wWorkflowItemService) {
        this.stammdatenRepository = stammdatenRepository;
        this.personalnummerService = personalnummerService;
        this.wWorkflowItemService = wWorkflowItemService;
    }

    @Override
    public List<Stammdaten> findAll() {
        return stammdatenRepository.findAll();
    }

    @Override
    public Page<Stammdaten> findAll(Pageable pageable) {
        return stammdatenRepository.findAll(pageable);
    }

    @Override
    public Page<MitarbeiterSummaryDto> findAllOrderedByNachnameEintritt(Pageable pageable, String mitarbeiterType, String wwgName) {

        return stammdatenRepository
                .findAllOrderedByNachnameEintritt(pageable, mitarbeiterType)
                .map(ob -> mapToMitarbeiterSummary(ob, wwgName));
    }

    @Override
    public Page<MitarbeiterSummaryDto> findForBenutzerOrderedByNachnameEintritt(Pageable pageable, String mitarbeiterType, String wwgName, String benutzerEmail) {

        return stammdatenRepository
                .findForBenutzerOrderedByNachnameEintritt(pageable, mitarbeiterType, benutzerEmail)
                .map(ob -> mapToMitarbeiterSummary(ob, wwgName));
    }

    @Override
    public List<Stammdaten> findAllByCreatedOnOrChangedOnAfter(LocalDateTime after) {
        return stammdatenRepository.findAllByCreatedOnAfterOrChangedOnAfter(after, after);
    }

    @Override
    public List<Stammdaten> findAllByVornameAndNachname(String vorname, String nachname) {
        return stammdatenRepository.findAllByVornameAndNachname(vorname, nachname);
    }

    @Override
    public List<Stammdaten> findAllByEmail(String email) {
        return stammdatenRepository.findAllByEmail(email);
    }

    @Override
    public Optional<Stammdaten> findById(Integer id) {
        return stammdatenRepository.findById(id);
    }

    @Override
    public Stammdaten save(Stammdaten object) {
        return stammdatenRepository.save(object);
    }

    @Override
    public List<Stammdaten> saveAll(List<Stammdaten> objects) {
        return stammdatenRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        stammdatenRepository.deleteById(id);
    }

    @Override
    public List<Stammdaten> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Stammdaten findByPersonalnummer(Personalnummer personalnummer) {
        return stammdatenRepository.findByPersonalnummer(personalnummer);
    }

    @Override
    public Stammdaten findByPersonalnummerId(Integer personalnummerId) {
        return stammdatenRepository.findByPersonalnummer_Id(personalnummerId);
    }

    @Override
    public Stammdaten findByPersonalnummerString(String personalnummer) {
        Personalnummer personalnummerObject = personalnummerService.findByPersonalnummer(personalnummer);
        if (personalnummerObject != null) {
            return stammdatenRepository.findByPersonalnummer(personalnummerObject);
        }
        return null;
    }

    @Override
    public Stammdaten findByPersonalnummerStringAndStatusIn(String personalnummer, List<MitarbeiterStatus> status) {
        return stammdatenRepository.findByPersonalnummer_PersonalnummerAndStatusIn(personalnummer, status);
    }

    @Override
    public Stammdaten findByPersonalnummerAndStatusIn(Personalnummer personalnummer, List<MitarbeiterStatus> status) {
        return stammdatenRepository.findByPersonalnummerAndStatusIn(personalnummer, status);
    }

    @Override
    public Page<MAFilteredResultDto> findMAByCriteria(MASearchCriteriaDto maSearchCriteriaDto, Pageable pageable) {
        return stammdatenRepository.findMAByCriteria(maSearchCriteriaDto, pageable);
    }

    @Override
    public StammdatenDto mapStammdatenToDto(Stammdaten stammdaten) {
        StammdatenDto stammdatenDto = new StammdatenDto();

        if (stammdaten.getId() != null) {
            stammdatenDto.setId(stammdaten.getId());
        }
        if (stammdaten.getPersonalnummer() != null) {
            if (stammdaten.getPersonalnummer().getPersonalnummer() != null) {
                stammdatenDto.setPersonalnummer(stammdaten.getPersonalnummer().getPersonalnummer());
            }
            if (stammdaten.getPersonalnummer().getFirma() != null && stammdaten.getPersonalnummer().getFirma().getName() != null) {
                stammdatenDto.setFirma(stammdaten.getPersonalnummer().getFirma().getName());
            }
        }
        if (stammdaten.getAnrede() != null) {
            stammdatenDto.setAnrede(stammdaten.getAnrede().getName());
        }
        if (stammdaten.getTitel() != null) {
            stammdatenDto.setTitel(stammdaten.getTitel().getName());
        }
        if (stammdaten.getTitel2() != null) {
            stammdatenDto.setTitel2(stammdaten.getTitel2().getName());
        }
        if (stammdaten.getNachname() != null) {
            stammdatenDto.setNachname(stammdaten.getNachname());
        }
        if (stammdaten.getVorname() != null) {
            stammdatenDto.setVorname(stammdaten.getVorname());
        }
        if (stammdaten.getGeburtsname() != null) {
            stammdatenDto.setGeburtsname(stammdaten.getGeburtsname());
        }
        if (stammdaten.getSvnr() != null) {
            stammdatenDto.setSvnr(getSvnForMitarbeiter(stammdaten.getSvnr()));
        }
        if (stammdaten.getEcardStatus() != null) {
            stammdatenDto.setEcard(stammdaten.getEcardStatus().getValue());
        }
        if (stammdaten.getGeschlecht() != null) {
            stammdatenDto.setGeschlecht(stammdaten.getGeschlecht().getName());
        }
        if (stammdaten.getFamilienstand() != null) {
            stammdatenDto.setFamilienstand(stammdaten.getFamilienstand().getName());
        }
        if (stammdaten.getGeburtsdatum() != null) {
            stammdatenDto.setGeburtsDatum(stammdaten.getGeburtsdatum().toString());
        }
        if (stammdaten.getLebensalter() != null) {
            stammdatenDto.setAlter(String.valueOf(stammdaten.getLebensalter()));
        }
        if (stammdaten.getStaatsbuergerschaft() != null) {
            stammdatenDto.setStaatsbuergerschaft(stammdaten.getStaatsbuergerschaft().getLandName());
        }
        stammdatenDto.setHandySignatur(stammdaten.isHandySignatur());

        if (stammdaten.getMuttersprache() != null) {
            stammdatenDto.setMuttersprache(stammdaten.getMuttersprache().getName());
        }
        if (stammdaten.getZusatzInfo() != null) {
            if (stammdaten.getZusatzInfo().getWien() != null) {
                stammdatenDto.setWien(stammdaten.getZusatzInfo().getWien());
            }
            if (stammdaten.getZusatzInfo().getNiederoesterreich() != null) {
                stammdatenDto.setNiederoesterreich(stammdaten.getZusatzInfo().getNiederoesterreich());
            }
            if (stammdaten.getZusatzInfo().getOberoesterreich() != null) {
                stammdatenDto.setOberoesterreich(stammdaten.getZusatzInfo().getOberoesterreich());
            }
            if (stammdaten.getZusatzInfo().getSalzburg() != null) {
                stammdatenDto.setSalzburg(stammdaten.getZusatzInfo().getSalzburg());
            }
            if (stammdaten.getZusatzInfo().getTirol() != null) {
                stammdatenDto.setTirol(stammdaten.getZusatzInfo().getTirol());
            }
            if (stammdaten.getZusatzInfo().getBurgenland() != null) {
                stammdatenDto.setBurgenland(stammdaten.getZusatzInfo().getBurgenland());
            }
            if (stammdaten.getZusatzInfo().getSteiermark() != null) {
                stammdatenDto.setSteiermark(stammdaten.getZusatzInfo().getSteiermark());
            }
            if (stammdaten.getZusatzInfo().getKaernten() != null) {
                stammdatenDto.setKaernten(stammdaten.getZusatzInfo().getKaernten());
            }
            if (stammdaten.getZusatzInfo().getVorarlberg() != null) {
                stammdatenDto.setVorarlberg(stammdaten.getZusatzInfo().getVorarlberg());
            }
            if (stammdaten.getZusatzInfo().getArbeitsgenehmigungStatus() != null) {
                stammdatenDto.setArbeitsgenehmigungDok(stammdaten.getZusatzInfo().getArbeitsgenehmigungStatus().getValue());
            }
            if (stammdaten.getZusatzInfo().getGueltigBis() != null) {
                stammdatenDto.setGueltigBis(stammdaten.getZusatzInfo().getGueltigBis().toString());
            }
            if (stammdaten.getZusatzInfo().getArbeitsgenehmigung() != null) {
                stammdatenDto.setArbeitsgenehmigung(stammdaten.getZusatzInfo().getArbeitsgenehmigung());
            }
            if (stammdaten.getZusatzInfo().getFoto() != null) {
                stammdatenDto.setFoto(stammdaten.getZusatzInfo().getFoto().getValue());
            }
        }
        if (stammdaten.getAdresse() != null) {
            if (stammdaten.getAdresse().getLand() != null) {
                stammdatenDto.setLand(stammdaten.getAdresse().getLand().getLandName());
            }
            if (stammdaten.getAdresse().getStrasse() != null) {
                stammdatenDto.setStrasse(stammdaten.getAdresse().getStrasse());
            }
            if (stammdaten.getAdresse().getPlz() != null) {
                stammdatenDto.setPlz(stammdaten.getAdresse().getPlz().getPlzString());
            }
            if (stammdaten.getAdresse().getOrt() != null) {
                stammdatenDto.setOrt(stammdaten.getAdresse().getOrt());
            }
        }
        if (stammdaten.getAbweichendeAdresse() != null) {
            if (stammdaten.getAbweichendeAdresse().getLand() != null) {
                stammdatenDto.setALand(stammdaten.getAbweichendeAdresse().getLand().getLandName());
            }
            if (stammdaten.getAbweichendeAdresse().getStrasse() != null) {
                stammdatenDto.setAStrasse(stammdaten.getAbweichendeAdresse().getStrasse());
            }
            if (stammdaten.getAbweichendeAdresse().getPlz() != null) {
                stammdatenDto.setAPlz(stammdaten.getAbweichendeAdresse().getPlz().getPlzString());
            }
            if (stammdaten.getAbweichendeAdresse().getOrt() != null) {
                stammdatenDto.setAOrt(stammdaten.getAbweichendeAdresse().getOrt());
            }
        }

        if (stammdaten.getEmail() != null) {
            stammdatenDto.setEmail(stammdaten.getEmail());
        }
        if (stammdaten.getMobilnummer() != null) {
            stammdatenDto.setMobilnummer(stammdaten.getMobilnummer().getLand().getTelefonvorwahl() + stammdaten.getMobilnummer().getTelefonnummer());
        }
        if (stammdaten.getBank() != null) {
            if (stammdaten.getBank().getBank() != null) {
                stammdatenDto.setBank(stammdaten.getBank().getBank());
            }
            if (stammdaten.getBank().getIban() != null) {
                stammdatenDto.setIban(stammdaten.getBank().getIban());
            }
            if (stammdaten.getBank().getBic() != null) {
                stammdatenDto.setBic(stammdaten.getBank().getBic());
            }
            if (stammdaten.getBank().getCard() != null) {
                stammdatenDto.setBankcard(stammdaten.getBank().getCard().getValue());
            }
        }
        for (StammdatenDataStatus dataStatus : stammdaten.getErrors()) {
            stammdatenDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
        }
        stammdatenDto.setErrors(new ArrayList<>(stammdatenDto.getErrorsMap().keySet()));
        return stammdatenDto;
    }

    private MitarbeiterSummaryDto mapToMitarbeiterSummary(Object[] row, String wwgName) {
        MitarbeiterSummaryDto mitarbeiterSummaryDto = new MitarbeiterSummaryDto();
        if (Objects.nonNull(row[0]) && !isNullOrBlank(String.valueOf(row[0]))) {
            mitarbeiterSummaryDto.setPersonalnummer(String.valueOf(row[0]));
        }
        if (Objects.nonNull(row[1]) && !isNullOrBlank(String.valueOf(row[1]))) {
            mitarbeiterSummaryDto.setNachname(String.valueOf(row[1]));
        }
        if (Objects.nonNull(row[2]) && !isNullOrBlank(String.valueOf(row[2]))) {
            mitarbeiterSummaryDto.setVorname(String.valueOf(row[2]));
        }
        if (Objects.nonNull(row[3]) && !isNullOrBlank(String.valueOf(row[3]))) {
            mitarbeiterSummaryDto.setSvnr(String.valueOf(row[3]));
        }
        if (Objects.nonNull(row[4]) && !isNullOrBlank(String.valueOf(row[4]))) {
            mitarbeiterSummaryDto.setEintritt(parseDate(String.valueOf(row[4])));
        }
        if (Objects.nonNull(row[5]) && !isNullOrBlank(String.valueOf(row[5]))) {
            mitarbeiterSummaryDto.setKostenstelle(String.valueOf(row[5]));
        }
        WorkflowItemDto workflowItemDto = wWorkflowItemService.findFirstIncompleteItemWithCompletedPredecessor(mitarbeiterSummaryDto.getPersonalnummer(), wwgName);
        mitarbeiterSummaryDto.setWorkflowItem(workflowItemDto);
        return mitarbeiterSummaryDto;
    }

}
