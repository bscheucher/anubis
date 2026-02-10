package com.ibosng.dbservice.services.impl.mitarbeiter;



import com.ibosng.dbservice.dtos.mitarbeiter.LvAcceptanceDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.LvAcceptance;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.repositories.mitarbeiter.LvAcceptanceRepository;
import com.ibosng.dbservice.services.mitarbeiter.LvAcceptanceService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
public class LvAcceptanceServiceImpl implements LvAcceptanceService {

    private final LvAcceptanceRepository lvAcceptanceRepository;
    private final PersonalnummerService personalnummerService;
    private final StammdatenService stammdatenService;

    public LvAcceptanceServiceImpl(LvAcceptanceRepository lvAcceptanceRepository,
                                   PersonalnummerService personalnummerService,
                                   StammdatenService stammdatenService) {
        this.lvAcceptanceRepository = lvAcceptanceRepository;
        this.personalnummerService = personalnummerService;
        this.stammdatenService = stammdatenService;
    }

    @Override
    public List<LvAcceptance> findAll() {
        return lvAcceptanceRepository.findAll();
    }

    @Override
    public Optional<LvAcceptance> findById(Integer id) {
        return lvAcceptanceRepository.findById(id);
    }

    @Override
    public LvAcceptance save(LvAcceptance object) {
        return lvAcceptanceRepository.save(object);
    }

    @Override
    public List<LvAcceptance> saveAll(List<LvAcceptance> objects) {
        return lvAcceptanceRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        lvAcceptanceRepository.deleteById(id);
    }

    @Override
    public List<LvAcceptance> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<LvAcceptance> findByPersonalnummer(String personalnummerString) {
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(personalnummerString);
        if(personalnummer != null) {
            return lvAcceptanceRepository.findByPersonalnummer(personalnummer);
        }
        return null;
    }
    @Override
    public LvAcceptanceDto mapLvAcceptanceToDto(LvAcceptance lvAcceptance) {
        LvAcceptanceDto lvAcceptanceDto = new LvAcceptanceDto();
        if(lvAcceptance.getPersonalnummer() != null && !isNullOrBlank(lvAcceptance.getPersonalnummer().getPersonalnummer())) {
            lvAcceptanceDto.setPersonalnummer(lvAcceptance.getPersonalnummer().getPersonalnummer());
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(lvAcceptance.getPersonalnummer().getPersonalnummer());
            if(stammdaten.getZusatzInfo() != null && stammdaten.getZusatzInfo().getArbeitsgenehmigungStatus() != null) {
                BlobStatus blobStatus = stammdaten.getZusatzInfo().getArbeitsgenehmigungStatus();
                if(blobStatus.equals(BlobStatus.VERIFIED) || blobStatus.equals(BlobStatus.NOT_VERIFIED)) {
                    lvAcceptanceDto.setHasArbeitsgenehmigung(true);
                }
            }

            if (stammdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
                if (stammdaten.getBank() == null || stammdaten.getBank().getCard() == null || BlobStatus.NONE.equals(stammdaten.getBank().getCard())) {
                    lvAcceptanceDto.setHasBankcard(false);
                }

                if (stammdaten.getEcardStatus() == null || BlobStatus.NONE.equals(stammdaten.getEcardStatus())) {
                    lvAcceptanceDto.setHasEcard(false);
                }
            }
        }
        if(!isNullOrBlank(lvAcceptance.getBankcardReason())) {
            lvAcceptanceDto.setBankcardReason(lvAcceptance.getBankcardReason());
        }
        lvAcceptanceDto.setBankcard(lvAcceptance.isBankcard());

        if(!isNullOrBlank(lvAcceptance.getEcardReason())) {
            lvAcceptanceDto.setEcardReason(lvAcceptance.getEcardReason());
        }
        lvAcceptanceDto.setEcard(lvAcceptance.isEcard());

        if(!isNullOrBlank(lvAcceptance.getArbeitsgenehmigungDokReason())) {
            lvAcceptanceDto.setArbeitsgenehmigungDokReason(lvAcceptance.getArbeitsgenehmigungDokReason());
        }
        lvAcceptanceDto.setArbeitsgenehmigungDok(lvAcceptance.isArbeitsgenehmigungDok());

        if(!isNullOrBlank(lvAcceptance.getGehaltEinstufungReason())) {
            lvAcceptanceDto.setGehaltEinstufungReason(lvAcceptance.getGehaltEinstufungReason());
        }
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(lvAcceptance.getPersonalnummer().getPersonalnummer());
        if(stammdaten != null && stammdaten.getStaatsbuergerschaft() != null && !stammdaten.getStaatsbuergerschaft().getIsInEuEeaCh()) {
            lvAcceptanceDto.setNeedsArbeitsgenehmigung(true);
        }

        lvAcceptanceDto.setGehaltEinstufung(lvAcceptance.isGehaltEinstufung());
        return lvAcceptanceDto;
    }
    @Override
    public LvAcceptance mapLvAcceptanceDtoToEntity(LvAcceptanceDto lvAcceptanceDto, LvAcceptance lvAcceptance) {
        if(lvAcceptance == null) {
            lvAcceptance = new LvAcceptance();
        }
        if(!isNullOrBlank(lvAcceptanceDto.getPersonalnummer())) {
            Personalnummer personalnummer = personalnummerService.findByPersonalnummer(lvAcceptanceDto.getPersonalnummer());
            if(personalnummer != null) {
                lvAcceptance.setPersonalnummer(personalnummer);
            }
        }
        if(!isNullOrBlank(lvAcceptanceDto.getBankcardReason())) {
            lvAcceptance.setBankcardReason(lvAcceptanceDto.getBankcardReason());
        }
        lvAcceptance.setBankcard(lvAcceptanceDto.isBankcard());

        if(!isNullOrBlank(lvAcceptanceDto.getEcardReason())) {
            lvAcceptance.setEcardReason(lvAcceptanceDto.getEcardReason());
        }
        lvAcceptance.setEcard(lvAcceptanceDto.isEcard());

        if(!isNullOrBlank(lvAcceptanceDto.getArbeitsgenehmigungDokReason())) {
            lvAcceptance.setArbeitsgenehmigungDokReason(lvAcceptanceDto.getArbeitsgenehmigungDokReason());
        }
        lvAcceptance.setArbeitsgenehmigungDok(lvAcceptanceDto.isArbeitsgenehmigungDok());

        if(!isNullOrBlank(lvAcceptanceDto.getGehaltEinstufungReason())) {
            lvAcceptance.setGehaltEinstufungReason(lvAcceptanceDto.getGehaltEinstufungReason());
        }
        lvAcceptance.setGehaltEinstufung(lvAcceptanceDto.isGehaltEinstufung());
        return lvAcceptance;
    }
}
