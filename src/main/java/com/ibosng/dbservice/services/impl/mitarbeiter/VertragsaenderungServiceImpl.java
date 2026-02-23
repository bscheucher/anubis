package com.ibosng.dbservice.services.impl.mitarbeiter;

import com.ibosng.dbservice.dtos.changelog.FieldChangeDto;
import com.ibosng.dbservice.dtos.changelog.VertragsaenderungChangeLogDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungBasicDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungOverviewDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsaenderung;
import com.ibosng.dbservice.entities.mitarbeiter.VertragsaenderungStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.repositories.mitarbeiter.VertragsaenderungRepository;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.changelog.MAChangeLogService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsaenderungService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
@Slf4j
public class VertragsaenderungServiceImpl implements VertragsaenderungService {



    private final PersonalnummerService personalnummerService;
    private final VertragsaenderungRepository vertragsaenderungRepository;
    private final MAChangeLogService changeLogService;
    private final BenutzerService benutzerService;

    @Override
    public List<Vertragsaenderung> findAllByPersonalnummerAndStatus(String personalnummer, VertragsaenderungStatus vertragsaenderungStatus) {
        return vertragsaenderungRepository.findByPersonalnummer_PersonalnummerAndStatus(personalnummer, vertragsaenderungStatus);
    }

    @Override
    public List<Vertragsaenderung> findAll() {
        return vertragsaenderungRepository.findAll();
    }

    @Override
    public Optional<Vertragsaenderung> findById(Integer id) {
        return vertragsaenderungRepository.findById(id);
    }

    @Override
    public Vertragsaenderung save(Vertragsaenderung object) {
        return vertragsaenderungRepository.save(object);
    }

    @Override
    public List<Vertragsaenderung> saveAll(List<Vertragsaenderung> objects) {
        return vertragsaenderungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        vertragsaenderungRepository.deleteById(id);
    }

    @Override
    public List<Vertragsaenderung> findAllByIdentifier(String identifier) {
        return null;
    }


    @Override
    public List<Vertragsaenderung> findAllByPersonalnummer_Personalnummer(String personalnummer) {
        return vertragsaenderungRepository.findAllByPersonalnummer_Personalnummer(personalnummer);
    }

    @Override
    public List<Vertragsaenderung> findAllByPersonalnummer_PersonalnummerAndStatus(String personalnummer, MitarbeiterStatus status) {
        return vertragsaenderungRepository.findAllByPersonalnummer_PersonalnummerAndStatus(personalnummer, status);
    }

    @Override
    public List<Vertragsaenderung> findAllBySuccessor_Id(Integer successorId) {
        return vertragsaenderungRepository.findAllBySuccessor_Id(successorId);
    }

    @Override
    public List<Vertragsaenderung> findAllByPredecessor_Id(Integer predecessorId) {
        return vertragsaenderungRepository.findAllByPredecessor_Id(predecessorId);
    }

    @Override
    public Vertragsaenderung findBySuccessor_IdAndPredecessor_Id(Integer successorId, Integer predecessorId) {
        return vertragsaenderungRepository.findBySuccessor_IdAndPredecessor_Id(successorId, predecessorId);
    }

    @Override
    public Page<VertragsaenderungOverviewDto> findAllOrderedAndFilteredForOverview(String searchTerm, List<String> statuses, Pageable pageable) {
        return vertragsaenderungRepository.findAllOrderedAndFilteredForOverview(searchTerm, statuses, pageable);
    }

    @Override
    public VertragsaenderungDto mapToVetragsaenderungDto(Vertragsaenderung vertragsaenderung) {
        VertragsaenderungDto vertragsaenderungDto = new VertragsaenderungDto();
        if (vertragsaenderung.getId() != null) {
            vertragsaenderungDto.setId(vertragsaenderung.getId());
        }
        if (vertragsaenderung.getPersonalnummer() != null && !isNullOrBlank(vertragsaenderung.getPersonalnummer().getPersonalnummer())) {
            vertragsaenderungDto.setPersonalnummer(vertragsaenderung.getPersonalnummer().getPersonalnummer());
        }
        if (vertragsaenderung.getAntragssteller() != null) {
            vertragsaenderungDto.setAntragssteller(vertragsaenderung.getAntragssteller().getFirstName() + " " + vertragsaenderung.getAntragssteller().getLastName());
        }
        if (vertragsaenderung.getGueltigAb() != null) {
            vertragsaenderungDto.setGueltigAb(vertragsaenderung.getGueltigAb());
        } else {
            vertragsaenderungDto.getErrors().add("gueltigAb");
            vertragsaenderungDto.getErrorsMap().put("gueltigAb", "Das Feld ist leer");
        }

        if (vertragsaenderung.getCreatedOn() != null) {
            vertragsaenderungDto.setCreatedAt(vertragsaenderung.getCreatedOn());
        }
        if (!isNullOrBlank(vertragsaenderung.getInterneAnmerkung())) {
            vertragsaenderungDto.setInterneAnmerkung(vertragsaenderung.getInterneAnmerkung());
        }
        if (!isNullOrBlank(vertragsaenderung.getOffizielleBemerkung())) {
            vertragsaenderungDto.setOffizielleBemerkung(vertragsaenderung.getOffizielleBemerkung());
        }
        if (!isNullOrBlank(vertragsaenderung.getKommentar())) {
            vertragsaenderungDto.setKommentar(vertragsaenderung.getKommentar());
        }
        if (vertragsaenderung.getSuccessor() != null) {
            vertragsaenderungDto.setSuccessor(vertragsaenderung.getSuccessor().getId());
        }
        if (vertragsaenderung.getPredecessor() != null) {
            vertragsaenderungDto.setPredecessor(vertragsaenderung.getPredecessor().getId());
        }
        if (vertragsaenderung.getStatus() != null) {
            vertragsaenderungDto.setStatus(vertragsaenderung.getStatus().name());
        }

        if(vertragsaenderung.getGenehmigender() != null) {
            vertragsaenderungDto.setGenehmigender(vertragsaenderung.getGenehmigender().getEmail());
        }
        if (vertragsaenderung.getSuccessor() != null && vertragsaenderung.getPredecessor() != null) {
/*            List<FieldChangeDto> fieldChangeDtos = entityChangeLogService.getChangeLogs(VERTRAGSDATEN, vertragsaenderung.getSuccessor().getId(), vertragsaenderung.getPredecessor().getId());
            fieldChangeDtos.forEach(fieldChangeDto -> {
                String fieldName = FieldNameMapper.getDtoFieldName(fieldChangeDto.getFieldName());
                if (!FieldNameMapper.isMappingExists(fieldChangeDto.getFieldName())) {
                    fieldChangeDto.setFieldName(
                            CaseFormat.LOWER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL)
                                    .convert(fieldChangeDto.getFieldName()));
                } else {
                    fieldChangeDto.setFieldName(fieldName);
                }
            });*/
            vertragsaenderungDto.setFieldChanges(changeLogService.getVertragsaenderungenPredecessorAndSuccessor(vertragsaenderung.getPredecessor().getId(), vertragsaenderung.getSuccessor().getId()));
        }

        List<String> recipients = vertragsaenderung.getAllRecipients().stream()
                .map(Benutzer::getEmail)
                .toList();
        vertragsaenderungDto.setEmailRecipients(recipients);

        return vertragsaenderungDto;
    }

    @Override
    public VertragsaenderungChangeLogDto getVertragsaenderungenChangeLog(String personalnummerString) {
        VertragsaenderungChangeLogDto vertragsaenderungChangeLog = new VertragsaenderungChangeLogDto();
        List<VertragsaenderungDto> vertragsaenderungDtos = new ArrayList<>();
        List<Vertragsaenderung> vertragsaenderungs = findAllByPersonalnummer_Personalnummer(personalnummerString);
        vertragsaenderungs.forEach(vertragsaenderung -> vertragsaenderungDtos.add(mapToVetragsaenderungDto(vertragsaenderung)));
        vertragsaenderungChangeLog.setVertragsaenderungen(vertragsaenderungDtos);
        return vertragsaenderungChangeLog;
    }

    @Override
    public VertragsaenderungBasicDto mapToVetragsaenderungBasicDto(Vertragsaenderung vertragsaenderung) {
        VertragsaenderungBasicDto vertragsaenderungDto = new VertragsaenderungBasicDto();
        if (vertragsaenderung.getId() != null) {
            vertragsaenderungDto.setId(vertragsaenderung.getId());
        }
        if (vertragsaenderung.getPersonalnummer() != null && !isNullOrBlank(vertragsaenderung.getPersonalnummer().getPersonalnummer())) {
            vertragsaenderungDto.setPersonalnummer(vertragsaenderung.getPersonalnummer().getPersonalnummer());
        }
        if (vertragsaenderung.getAntragssteller() != null) {
            vertragsaenderungDto.setAntragssteller(vertragsaenderung.getAntragssteller().getFirstName() + " " + vertragsaenderung.getAntragssteller().getLastName());
        }
        if (vertragsaenderung.getGueltigAb() != null) {
            vertragsaenderungDto.setGueltigAb(vertragsaenderung.getGueltigAb());
        }

        return vertragsaenderungDto;
    }

    @Override
    public Vertragsaenderung updateVertragsaenderung(VertragsaenderungDto vertragsaenderungDto) {
        Vertragsaenderung vertragsaenderung = vertragsaenderungRepository.findById(vertragsaenderungDto.getId()).orElse(null);
        if (vertragsaenderung == null) {
            return null;
        }

        LocalDate gueltigAb = vertragsaenderungDto.getGueltigAb();
        if (gueltigAb != null) {
            vertragsaenderung.setGueltigAb(gueltigAb);
        }

        String interneAnmerkung = vertragsaenderungDto.getInterneAnmerkung();
        if (!isNullOrBlank(interneAnmerkung)) {
            vertragsaenderung.setInterneAnmerkung(interneAnmerkung);
        }

        String offizielleBemerkung = vertragsaenderungDto.getOffizielleBemerkung();
        if (!isNullOrBlank(offizielleBemerkung)) {
            vertragsaenderung.setOffizielleBemerkung(offizielleBemerkung);
        }

        String kommentar = vertragsaenderungDto.getKommentar();
        if (!isNullOrBlank(kommentar)) {
            vertragsaenderung.setKommentar(kommentar);
        }
        vertragsaenderung.setChangedOn(getLocalDateNow());
        vertragsaenderung = save(vertragsaenderung);
        setEmails(vertragsaenderung, vertragsaenderungDto);

        return vertragsaenderung;
    }


    @Override
    public List<FieldChangeDto> compareVertragsdaten(VertragsdatenDto vertragsdatenOld, VertragsdatenDto vertragsdatenNew) {
        List<FieldChangeDto> fieldChangeDtos = new ArrayList<>();

        //kostenstelle
        if (!Objects.equals(vertragsdatenOld.getKostenstelle(), vertragsdatenNew.getKostenstelle()) &&
                !isNullOrBlank(vertragsdatenNew.getKostenstelle())) {
            fieldChangeDtos.add(new FieldChangeDto("kostenstelle", vertragsdatenOld.getKostenstelle(), vertragsdatenNew.getKostenstelle()));
        }

        //arbeitsort
        if (!Objects.equals(vertragsdatenNew.getDienstort(), vertragsdatenOld.getDienstort()) &&
                !isNullOrBlank(vertragsdatenNew.getDienstort())) {
            fieldChangeDtos.add(new FieldChangeDto("dienstort", vertragsdatenOld.getDienstort(), vertragsdatenNew.getDienstort()));
        }

        //wochenstunden
        if (!Objects.equals(vertragsdatenNew.getWochenstunden(), vertragsdatenOld.getWochenstunden()) &&
                !isNullOrBlank(vertragsdatenNew.getWochenstunden())) {
            fieldChangeDtos.add(new FieldChangeDto("wochenstunden", vertragsdatenOld.getWochenstunden(), vertragsdatenNew.getWochenstunden()));
        }

        //arbeitszeiten
        compareArbeitszeiten(fieldChangeDtos, vertragsdatenOld, vertragsdatenNew);
        compareArbeitszeitenInfo(fieldChangeDtos, vertragsdatenOld, vertragsdatenNew);

        //gehalt vereinbart
        if (!Objects.equals(vertragsdatenNew.getGehaltVereinbart(), vertragsdatenOld.getGehaltVereinbart()) &&
                vertragsdatenNew.getGehaltVereinbart() != null) {
            fieldChangeDtos.add(new FieldChangeDto("gehaltVereinbart", String.valueOf(vertragsdatenOld.getGehaltVereinbart()), String.valueOf(vertragsdatenNew.getGehaltVereinbart())));
        }

        //zulagen
        if (!Objects.equals(vertragsdatenNew.getZulageInEuroFix(), vertragsdatenOld.getZulageInEuroFix()) &&
                !isNullOrBlank(vertragsdatenNew.getZulageInEuroFix())) {
            fieldChangeDtos.add(new FieldChangeDto("zulageInEuroFix", String.valueOf(vertragsdatenOld.getZulageInEuroFix()), String.valueOf(vertragsdatenNew.getZulageInEuroFix())));
        }
        if (!Objects.equals(vertragsdatenNew.getZulageInEuroFunktion(), vertragsdatenOld.getZulageInEuroFunktion()) &&
                !isNullOrBlank(vertragsdatenNew.getZulageInEuroFunktion())) {
            fieldChangeDtos.add(new FieldChangeDto("zulageInEuroFunktion", String.valueOf(vertragsdatenOld.getZulageInEuroFunktion()), String.valueOf(vertragsdatenNew.getZulageInEuroFunktion())));
        }
        if (!Objects.equals(vertragsdatenNew.getZulageInEuroLeitung(), vertragsdatenOld.getZulageInEuroLeitung()) &&
                !isNullOrBlank(vertragsdatenNew.getZulageInEuroLeitung())) {
            fieldChangeDtos.add(new FieldChangeDto("zulageInEuroLeitung", String.valueOf(vertragsdatenOld.getZulageInEuroFunktion()), String.valueOf(vertragsdatenNew.getZulageInEuroFunktion())));
        }

        //verwendungsbereich
        if (!Objects.equals(vertragsdatenNew.getVerwendungsbereichsaenderung(), vertragsdatenOld.getVerwendungsbereichsaenderung()) &&
                !isNullOrBlank(vertragsdatenNew.getVerwendungsbereichsaenderung())) {
            fieldChangeDtos.add(new FieldChangeDto("verwendungsbereichsaenderung", String.valueOf(vertragsdatenOld.getVerwendungsbereichsaenderung()), String.valueOf(vertragsdatenNew.getVerwendungsbereichsaenderung())));
        }

        //jobbezeichnung
        if (!Objects.equals(vertragsdatenNew.getJobBezeichnung(), vertragsdatenOld.getJobBezeichnung()) &&
                !isNullOrBlank(vertragsdatenNew.getJobBezeichnung())) {
            fieldChangeDtos.add(new FieldChangeDto("jobBezeichnung", vertragsdatenOld.getJobBezeichnung(), vertragsdatenNew.getJobBezeichnung()));
        }

        //befristung
        if (!Objects.equals(vertragsdatenNew.getBefristungBis(), vertragsdatenOld.getBefristungBis()) &&
                !isNullOrBlank(vertragsdatenNew.getBefristungBis())) {
            fieldChangeDtos.add(new FieldChangeDto("befristungBis", vertragsdatenOld.getBefristungBis(), vertragsdatenNew.getBefristungBis()));
        }

        //umbuchungsmodell
        if (!Objects.equals(vertragsdatenNew.getArbeitszeitmodell(), vertragsdatenOld.getArbeitszeitmodell()) &&
                !isNullOrBlank(vertragsdatenNew.getArbeitszeitmodell())) {
            fieldChangeDtos.add(new FieldChangeDto("arbeitszeiten", vertragsdatenOld.getArbeitszeitmodell(), vertragsdatenNew.getArbeitszeitmodell()));
        }

        //mobile working
        if (!Objects.equals(vertragsdatenNew.getMobileWorking(), vertragsdatenOld.getMobileWorking()) &&
                vertragsdatenNew.getMobileWorking() != null) {
            fieldChangeDtos.add(new FieldChangeDto("mobileWorking", String.valueOf(vertragsdatenOld.getMobileWorking()), String.valueOf(vertragsdatenNew.getMobileWorking())));
        }

        return fieldChangeDtos;
    }

    private void compareArbeitszeitenInfo(List<FieldChangeDto> fieldChangeDtos, VertragsdatenDto oldData, VertragsdatenDto newData) {
        compareField(fieldChangeDtos, "stundenaenderung", oldData.getStundenaenderung(), newData.getStundenaenderung());
        compareField(fieldChangeDtos, "verwendungsbereichsaenderung", oldData.getVerwendungsbereichsaenderung(), newData.getVerwendungsbereichsaenderung());
        compareField(fieldChangeDtos, "stufenwechsel", oldData.getStufenwechsel(), newData.getStufenwechsel());
        compareField(fieldChangeDtos, "geschaeftsbereichsaenderung", oldData.getGeschaeftsbereichsaenderung(), newData.getGeschaeftsbereichsaenderung());
        compareField(fieldChangeDtos, "kvErhoehung", String.valueOf(oldData.getKvErhoehung()), String.valueOf(newData.getKvErhoehung()));
        compareField(fieldChangeDtos, "notizArbeitszeit", oldData.getNotizArbeitszeit(), newData.getNotizArbeitszeit());
    }

    private void compareArbeitszeiten(List<FieldChangeDto> fieldChangeDtos, VertragsdatenDto oldData, VertragsdatenDto newData) {
        compareField(fieldChangeDtos, "aMontagVon", oldData.getAMontagVon(), newData.getAMontagVon());
        compareField(fieldChangeDtos, "aMontagBis", oldData.getAMontagBis(), newData.getAMontagBis());
        compareField(fieldChangeDtos, "aMontagNetto", oldData.getAMontagNetto(), newData.getAMontagNetto());
        compareField(fieldChangeDtos, "aDienstagVon", oldData.getADienstagVon(), newData.getADienstagVon());
        compareField(fieldChangeDtos, "aDienstagBis", oldData.getADienstagBis(), newData.getADienstagBis());
        compareField(fieldChangeDtos, "aDienstagNetto", oldData.getADienstagNetto(), newData.getADienstagNetto());
        compareField(fieldChangeDtos, "aMittwochVon", oldData.getAMittwochVon(), newData.getAMittwochVon());
        compareField(fieldChangeDtos, "aMittwochBis", oldData.getAMittwochBis(), newData.getAMittwochBis());
        compareField(fieldChangeDtos, "aMittwochNetto", oldData.getAMittwochNetto(), newData.getAMittwochNetto());
        compareField(fieldChangeDtos, "aDonnerstagVon", oldData.getADonnerstagVon(), newData.getADonnerstagVon());
        compareField(fieldChangeDtos, "aDonnerstagBis", oldData.getADonnerstagBis(), newData.getADonnerstagBis());
        compareField(fieldChangeDtos, "aDonnerstagNetto", oldData.getADonnerstagNetto(), newData.getADonnerstagNetto());
        compareField(fieldChangeDtos, "aFreitagVon", oldData.getAFreitagVon(), newData.getAFreitagVon());
        compareField(fieldChangeDtos, "aFreitagBis", oldData.getAFreitagBis(), newData.getAFreitagBis());
        compareField(fieldChangeDtos, "aFreitagNetto", oldData.getAFreitagNetto(), newData.getAFreitagNetto());
        compareField(fieldChangeDtos, "kMontagVon", oldData.getKMontagVon(), newData.getKMontagVon());
        compareField(fieldChangeDtos, "kMontagBis", oldData.getKMontagBis(), newData.getKMontagBis());
        compareField(fieldChangeDtos, "kDienstagVon", oldData.getKDienstagVon(), newData.getKDienstagVon());
        compareField(fieldChangeDtos, "kDienstagBis", oldData.getKDienstagBis(), newData.getKDienstagBis());
        compareField(fieldChangeDtos, "kMittwochVon", oldData.getKMittwochVon(), newData.getKMittwochVon());
        compareField(fieldChangeDtos, "kMittwochBis", oldData.getKMittwochBis(), newData.getKMittwochBis());
        compareField(fieldChangeDtos, "kDonnerstagVon", oldData.getKDonnerstagVon(), newData.getKDonnerstagVon());
        compareField(fieldChangeDtos, "kDonnerstagBis", oldData.getKDonnerstagBis(), newData.getKDonnerstagBis());
        compareField(fieldChangeDtos, "kFreitagVon", oldData.getKFreitagVon(), newData.getKFreitagVon());
        compareField(fieldChangeDtos, "kFreitagBis", oldData.getKFreitagBis(), newData.getKFreitagBis());
    }

    private void compareField(List<FieldChangeDto> fieldChangeDtos, String fieldName, String oldValue, String newValue) {
        if (!Objects.equals(oldValue, newValue) && !isNullOrBlank(newValue)) {
            fieldChangeDtos.add(new FieldChangeDto(fieldName, oldValue, newValue));
        }
    }


    @Override
    public Vertragsaenderung map(VertragsaenderungDto vertragsaenderungDto, Benutzer antragssteller,
                                 Vertragsdaten oldVertragsdaten, Vertragsdaten newVertragsdaten) {
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(vertragsaenderungDto.getPersonalnummer());
        Vertragsaenderung vertragsaenderung = new Vertragsaenderung();
        vertragsaenderung.setSuccessor(newVertragsdaten);
        vertragsaenderung.setPredecessor(oldVertragsdaten);
        vertragsaenderung.setPersonalnummer(personalnummer);
        vertragsaenderung.setAntragssteller(antragssteller);
        vertragsaenderung.setId(vertragsaenderungDto.getId());
        vertragsaenderung.setStatus(VertragsaenderungStatus.NEW);

        LocalDate gueltigAb = vertragsaenderungDto.getGueltigAb();
        if (gueltigAb != null) {
            vertragsaenderung.setGueltigAb(gueltigAb);
        }

        String interneAnmerkung = vertragsaenderungDto.getInterneAnmerkung();
        if (!isNullOrBlank(interneAnmerkung)) {
            vertragsaenderung.setInterneAnmerkung(interneAnmerkung);
        }

        String offizielleBemerkung = vertragsaenderungDto.getOffizielleBemerkung();
        if (!isNullOrBlank(offizielleBemerkung)) {
            vertragsaenderung.setOffizielleBemerkung(offizielleBemerkung);
        }

        String kommentar = vertragsaenderungDto.getKommentar();
        if (!isNullOrBlank(kommentar)) {
            vertragsaenderung.setKommentar(kommentar);
        }
        vertragsaenderung.setCreatedOn(getLocalDateNow());
        return vertragsaenderung;
    }

    @Override
    public Vertragsaenderung setEmails(Vertragsaenderung vertragsaenderung, VertragsaenderungDto vertragsaenderungDto) {
        for (String email : vertragsaenderungDto.getEmailRecipients()) {
            Benutzer benutzer = benutzerService.findByEmail(email);
            if (benutzer != null) {
                vertragsaenderung.getAllRecipients().add(benutzer);
            }
        }

        return vertragsaenderungRepository.save(vertragsaenderung);
    }



}
