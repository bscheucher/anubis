package com.ibosng.validationservice.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VertragsdatenDataStatus;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.services.BaseService;
import com.ibosng.dbservice.services.masterdata.KVStufeService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoZulageService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.Validation;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten.*;
import com.ibosng.validationservice.services.VertragsdatenValidatorService;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.teilnehmer.validations.vertragsdaten.TeilnehmerArbeitszeitenValidation;
import com.ibosng.validationservice.teilnehmer.validations.vertragsdaten.TeilnehmerLehrjahrValidation;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterVertragsdatenValidator extends SingleEntityAbstractValidator<VertragsdatenDto, Vertragsdaten> {
    private final VertragsdatenValidatorService vertragsdatenValidatorService;
    private final GehaltInfoService gehaltInfoService;
    private final GehaltInfoZulageService gehaltInfoZulageService;
    private final PersonalnummerService personalnummerService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final KVStufeService kvStufeService;

    private final MitarbeiterEintrittValidation mitarbeiterEintrittValidation;
    private final MitarbeiterBefristungBisValidation mitarbeiterBefristungBisValidation;
    private final MitarbeiterWochenstundenValidation mitarbeiterWochenstundenValidation;
    private final MitarbeiterVerwendungsgruppeValidation mitarbeiterVerwendungsgruppeValidation;
    private final MitarbeiterKollektivvertragValidation mitarbeiterKollektivvertragValidation;
    private final MitarbeiterArbeitszeitmodellBisValidation mitarbeiterArbeitszeitmodellBisValidation;
    private final MitarbeiterArbeitszeitenValidation mitarbeiterArbeitszeitenValidation;
    private final TeilnehmerArbeitszeitenValidation teilnehmerArbeitszeitenValidation;
    private final MitarbeiterKategorieValidation mitarbeiterKategorieValidation;
    private final MitarbeiterTaetigkeitValidation mitarbeiterTaetigkeitValidation;
    private final MitarbeiterJobBezeichnungValidation mitarbeiterJobBezeichnungValidation;
    private final MitarbeiterDienstortValidation mitarbeiterDienstortValidation;
    private final MitarbeiterGehaltUeberstundenValidation mitarbeiterGehaltUeberstundenValidation;
    private final MitarbeiterUestPauschaleValidation mitarbeiterUestPauschaleValidation;
    private final MitarbeiterZusatzVereinbarungValidation mitarbeiterZusatzVereinbarungValidation;
    private final MitarbeiterDienstnehmergruppeValidation mitarbeiterDienstnehmergruppeValidation;
    private final MitarbeiterAbrechnungsgruppeValidation mitarbeiterAbrechnungsgruppeValidation;
    private final MitarbeiterFuehrungskraftValidation mitarbeiterFuehrungskraftValidation;
    private final MitarbeiterStartcoachValidation mitarbeiterStartcoachValidation;
    private final MitarbeiterKostenstelleValidation mitarbeiterKostenstelleValidation;
    private final TeilnehmerLehrjahrValidation teilnehmerLehrjahrValidation;
    private final MitarbeiterArbeitszeitenInfoValidation mitarbeiterArbeitszeitenInfoValidation;
    private final MitarbeiterKlasseValidation mitarbeiterKlasseValidation;
    private final MitarbeiterNaechsteVorrueckungValidation mitarbeiterNaechsteVorrueckungValidation;
    private final GlobalUserHolder globalUserHolder;

    private static final String FIX_ZULAGE = "Fixzulage";
    private static final String FUNKTIONS_ZULAGE = "Funktionszulage";
    private static final String LEITUNGS_ZULAGE = "Leitungszulage";

    protected boolean isTeilnehmer;

    public MitarbeiterVertragsdatenValidator(BaseService<Vertragsdaten> baseServiceV,
                                             VertragsdatenValidatorService vertragsdatenValidatorService,
                                             GehaltInfoService gehaltInfoService,
                                             GehaltInfoZulageService gehaltInfoZulageService,
                                             PersonalnummerService personalnummerService,
                                             ArbeitszeitenInfoService arbeitszeitenInfoService,
                                             KVStufeService kvStufeService,
                                             MitarbeiterEintrittValidation mitarbeiterEintrittValidation,
                                             MitarbeiterBefristungBisValidation mitarbeiterBefristungBisValidation,
                                             MitarbeiterWochenstundenValidation mitarbeiterWochenstundenValidation,
                                             MitarbeiterVerwendungsgruppeValidation mitarbeiterVerwendungsgruppeValidation,
                                             MitarbeiterKollektivvertragValidation mitarbeiterKollektivvertragValidation,
                                             MitarbeiterArbeitszeitmodellBisValidation mitarbeiterArbeitszeitmodellBisValidation,
                                             MitarbeiterArbeitszeitenValidation mitarbeiterArbeitszeitenValidation,
                                             TeilnehmerArbeitszeitenValidation teilnehmerArbeitszeitenValidation,
                                             MitarbeiterKategorieValidation mitarbeiterKategorieValidation,
                                             MitarbeiterTaetigkeitValidation mitarbeiterTaetigkeitValidation,
                                             MitarbeiterJobBezeichnungValidation mitarbeiterJobBezeichnungValidation,
                                             MitarbeiterDienstortValidation mitarbeiterDienstortValidation,
                                             MitarbeiterGehaltUeberstundenValidation mitarbeiterGehaltUeberstundenValidation,
                                             MitarbeiterUestPauschaleValidation mitarbeiterUestPauschaleValidation,
                                             MitarbeiterZusatzVereinbarungValidation mitarbeiterZusatzVereinbarungValidation,
                                             MitarbeiterDienstnehmergruppeValidation mitarbeiterDienstnehmergruppeValidation,
                                             MitarbeiterAbrechnungsgruppeValidation mitarbeiterAbrechnungsgruppeValidation,
                                             MitarbeiterFuehrungskraftValidation mitarbeiterFuehrungskraftValidation,
                                             MitarbeiterStartcoachValidation mitarbeiterStartcoachValidation,
                                             MitarbeiterKostenstelleValidation mitarbeiterKostenstelleValidation,
                                             TeilnehmerLehrjahrValidation teilnehmerLehrjahrValidation, MitarbeiterArbeitszeitenInfoValidation mitarbeiterArbeitszeitenInfoValidation, MitarbeiterKlasseValidation mitarbeiterKlasseValidation, MitarbeiterNaechsteVorrueckungValidation mitarbeiterNaechsteVorrueckungValidation, GlobalUserHolder globalUserHolder) {
        super(baseServiceV);
        this.vertragsdatenValidatorService = vertragsdatenValidatorService;
        this.gehaltInfoService = gehaltInfoService;
        this.gehaltInfoZulageService = gehaltInfoZulageService;
        this.personalnummerService = personalnummerService;
        this.arbeitszeitenInfoService = arbeitszeitenInfoService;
        this.kvStufeService = kvStufeService;
        this.mitarbeiterEintrittValidation = mitarbeiterEintrittValidation;
        this.mitarbeiterBefristungBisValidation = mitarbeiterBefristungBisValidation;
        this.mitarbeiterWochenstundenValidation = mitarbeiterWochenstundenValidation;
        this.mitarbeiterVerwendungsgruppeValidation = mitarbeiterVerwendungsgruppeValidation;
        this.mitarbeiterKollektivvertragValidation = mitarbeiterKollektivvertragValidation;
        this.mitarbeiterArbeitszeitmodellBisValidation = mitarbeiterArbeitszeitmodellBisValidation;
        this.mitarbeiterArbeitszeitenValidation = mitarbeiterArbeitszeitenValidation;
        this.teilnehmerArbeitszeitenValidation = teilnehmerArbeitszeitenValidation;
        this.mitarbeiterKategorieValidation = mitarbeiterKategorieValidation;
        this.mitarbeiterTaetigkeitValidation = mitarbeiterTaetigkeitValidation;
        this.mitarbeiterJobBezeichnungValidation = mitarbeiterJobBezeichnungValidation;
        this.mitarbeiterDienstortValidation = mitarbeiterDienstortValidation;
        this.mitarbeiterGehaltUeberstundenValidation = mitarbeiterGehaltUeberstundenValidation;
        this.mitarbeiterUestPauschaleValidation = mitarbeiterUestPauschaleValidation;
        this.mitarbeiterZusatzVereinbarungValidation = mitarbeiterZusatzVereinbarungValidation;
        this.mitarbeiterDienstnehmergruppeValidation = mitarbeiterDienstnehmergruppeValidation;
        this.mitarbeiterAbrechnungsgruppeValidation = mitarbeiterAbrechnungsgruppeValidation;
        this.mitarbeiterFuehrungskraftValidation = mitarbeiterFuehrungskraftValidation;
        this.mitarbeiterStartcoachValidation = mitarbeiterStartcoachValidation;
        this.mitarbeiterKostenstelleValidation = mitarbeiterKostenstelleValidation;
        this.teilnehmerLehrjahrValidation = teilnehmerLehrjahrValidation;
        this.mitarbeiterArbeitszeitenInfoValidation = mitarbeiterArbeitszeitenInfoValidation;
        this.mitarbeiterKlasseValidation = mitarbeiterKlasseValidation;
        this.mitarbeiterNaechsteVorrueckungValidation = mitarbeiterNaechsteVorrueckungValidation;
        this.globalUserHolder = globalUserHolder;
    }

    @Override
    protected void prepare() {
        addValidation(mitarbeiterEintrittValidation);
        addValidation(mitarbeiterBefristungBisValidation);
        addValidation(mitarbeiterWochenstundenValidation);
        addValidation(mitarbeiterVerwendungsgruppeValidation);
        addValidation(mitarbeiterKollektivvertragValidation);
        addValidation(mitarbeiterArbeitszeitenInfoValidation);
        addValidation(mitarbeiterArbeitszeitenValidation);
        addValidation(teilnehmerArbeitszeitenValidation);
        addValidation(mitarbeiterKategorieValidation);
        addValidation(mitarbeiterTaetigkeitValidation);
        addValidation(mitarbeiterJobBezeichnungValidation);
        addValidation(mitarbeiterDienstortValidation);
        addValidation(mitarbeiterGehaltUeberstundenValidation);
        addValidation(mitarbeiterUestPauschaleValidation);
        addValidation(mitarbeiterZusatzVereinbarungValidation);
        addValidation(mitarbeiterDienstnehmergruppeValidation);
        addValidation(mitarbeiterAbrechnungsgruppeValidation);
        addValidation(mitarbeiterFuehrungskraftValidation);
        addValidation(mitarbeiterStartcoachValidation);
        addValidation(mitarbeiterKostenstelleValidation);
        addValidation(mitarbeiterKlasseValidation);
        addValidation(teilnehmerLehrjahrValidation);
        addValidation(mitarbeiterNaechsteVorrueckungValidation);
    }

    @Override
    protected void executeValidations() {
        for (VertragsdatenDto object : this.objectsToValidate) {
            Personalnummer personalnummer = personalnummerService.findByPersonalnummer(object.getPersonalnummer());
            isTeilnehmer = (personalnummer != null) && MitarbeiterType.TEILNEHMER.equals(personalnummer.getMitarbeiterType());

            Vertragsdaten validatedObject = vertragsdatenValidatorService.getVertragsdaten(object, getIsOnboarding());
            if (validatedObject != null) {
                boolean validationsResult = executeValidationsForOneObject(object, validatedObject);
                validatedObject = baseServiceV.save(validatedObject);
                validationResults.put(new ValidationObjectPair<>(object, validatedObject), validationsResult);
            }
        }
    }

    @Override
    protected List<VertragsdatenDto> postProcess() {
        List<VertragsdatenDto> processedVertragsdaten = new ArrayList<>();
        for (Map.Entry<ValidationObjectPair<VertragsdatenDto, Vertragsdaten>, Boolean> entry : validationResults.entrySet()) {
            Vertragsdaten vertragsdaten = entry.getKey().getSecond();
            VertragsdatenDto vertragsdatenDto = entry.getKey().getFirst();
            for (VertragsdatenDataStatus dataStatus : vertragsdaten.getErrors()) {
                vertragsdatenDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
            }
            vertragsdatenDto.setErrors(new ArrayList<>(vertragsdatenDto.getErrorsMap().keySet()));
            if (Boolean.TRUE.equals(entry.getValue())) {
                setStatusAndSaveVertragsdaten(vertragsdaten, vertragsdatenDto, MitarbeiterStatus.VALIDATED);
            } else {
                setStatusAndSaveVertragsdaten(vertragsdaten, vertragsdatenDto, MitarbeiterStatus.NOT_VALIDATED);
            }
            processedVertragsdaten.add(vertragsdatenDto);
        }
        return processedVertragsdaten;
    }

    private void setStatusToVertragsdatenRelatedtables(Vertragsdaten vertragsdaten, MitarbeiterStatus status) {
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());

        arbeitszeitenInfo.setChangedBy(getChangedBy());
        arbeitszeitenInfo.setChangedOn(getLocalDateNow());
        arbeitszeitenInfo.setStatus(status);

        arbeitszeitenInfoService.save(arbeitszeitenInfo);

        GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (gehaltInfo != null) {
            gehaltInfo.setChangedBy(getChangedBy());
            gehaltInfo.setChangedOn(getLocalDateNow());
            gehaltInfo.setStatus(status);
            gehaltInfoService.save(gehaltInfo);
        }
    }

    private void setStatusAndSaveVertragsdaten(Vertragsdaten vertragsdaten, VertragsdatenDto vertragsdatenDto, MitarbeiterStatus status) {
        vertragsdaten.setStatus(status);
        vertragsdaten.getPersonalnummer().setStatus(Status.ACTIVE);
        vertragsdaten.setChangedBy(getChangedBy());
        vertragsdaten.setChangedOn(getLocalDateNow());
        setRestFields(vertragsdaten, vertragsdatenDto);
        vertragsdaten.setNotizAllgemein(vertragsdatenDto.getNotizAllgemein());
        Vertragsdaten savedVertragsdaten = baseServiceV.save(vertragsdaten);
        vertragsdatenDto.setId(savedVertragsdaten.getId());
        setStatusToVertragsdatenRelatedtables(vertragsdaten, status);
        setAndSaveZulage(savedVertragsdaten, vertragsdatenDto, status);
    }

    private void setAndSaveZulage(Vertragsdaten vertragsdaten, VertragsdatenDto vertragsdatenDto, MitarbeiterStatus status) {
        GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (gehaltInfo != null && (MitarbeiterStatus.NEW.equals(gehaltInfo.getStatus()) || status.equals(gehaltInfo.getStatus()))) {
            // Fixzulage
            if (vertragsdatenDto.getFixZulage() != null && vertragsdatenDto.getFixZulage()) {
                //Only persist if valid value for ZulageInEuro
                if (NumberUtils.isCreatable(vertragsdatenDto.getZulageInEuroFix())) {
                    Optional<GehaltInfoZulage> foundEntry = gehaltInfoZulageService.findByArtDerZulageAndGehaltInfoId(FIX_ZULAGE, gehaltInfo.getId());
                    if (foundEntry.isPresent()) {
                        updateGehaltInfoZulage(foundEntry.get(), vertragsdatenDto.getZulageInEuroFix());
                        gehaltInfoZulageService.save(foundEntry.get());
                    } else {
                        GehaltInfoZulage toPersist = mapToGehaltInfoZulage(gehaltInfo, FIX_ZULAGE, vertragsdatenDto.getZulageInEuroFix());
                        toPersist = gehaltInfoZulageService.save(toPersist);
                    }
                }
            } else if (vertragsdatenDto.getFixZulage() != null && !vertragsdatenDto.getFixZulage()) {
                gehaltInfoZulageService.deleteByArtDerZulageAndGehaltInfoId(FIX_ZULAGE, gehaltInfo.getId());
            }

            // Funktionszulage
            if (vertragsdatenDto.getFunktionsZulage() != null && vertragsdatenDto.getFunktionsZulage()) {
                //Only persist if valid value for ZulageInEuro
                if (NumberUtils.isCreatable(vertragsdatenDto.getZulageInEuroFunktion())) {
                    Optional<GehaltInfoZulage> foundEntry = gehaltInfoZulageService.findByArtDerZulageAndGehaltInfoId(FUNKTIONS_ZULAGE, gehaltInfo.getId());
                    if (foundEntry.isPresent()) {
                        updateGehaltInfoZulage(foundEntry.get(), vertragsdatenDto.getZulageInEuroFunktion());
                        gehaltInfoZulageService.save(foundEntry.get());
                    } else {
                        GehaltInfoZulage toPersist = mapToGehaltInfoZulage(gehaltInfo, FUNKTIONS_ZULAGE, vertragsdatenDto.getZulageInEuroFunktion());
                        toPersist = gehaltInfoZulageService.save(toPersist);
                    }
                }
            } else if ((vertragsdatenDto.getFunktionsZulage() != null && !vertragsdatenDto.getFunktionsZulage())) {
                gehaltInfoZulageService.deleteByArtDerZulageAndGehaltInfoId(FUNKTIONS_ZULAGE, gehaltInfo.getId());
            }

            //Leitungszulage
            if (vertragsdatenDto.getLeitungsZulage() != null && vertragsdatenDto.getLeitungsZulage()) {
                //Only persist if valid value for ZulageInEuro
                if (NumberUtils.isCreatable(vertragsdatenDto.getZulageInEuroLeitung())) {
                    Optional<GehaltInfoZulage> foundEntry = gehaltInfoZulageService.findByArtDerZulageAndGehaltInfoId(LEITUNGS_ZULAGE, gehaltInfo.getId());
                    if (foundEntry.isPresent()) {
                        updateGehaltInfoZulage(foundEntry.get(), vertragsdatenDto.getZulageInEuroLeitung());
                        gehaltInfoZulageService.save(foundEntry.get());
                    } else {
                        GehaltInfoZulage toPersist = mapToGehaltInfoZulage(gehaltInfo, LEITUNGS_ZULAGE, vertragsdatenDto.getZulageInEuroLeitung());
                        toPersist = gehaltInfoZulageService.save(toPersist);
                    }
                }
            } else if (vertragsdatenDto.getLeitungsZulage() != null && !vertragsdatenDto.getLeitungsZulage()) {
                gehaltInfoZulageService.deleteByArtDerZulageAndGehaltInfoId(LEITUNGS_ZULAGE, gehaltInfo.getId());
            }
            if (vertragsdaten.getErrors().stream().noneMatch(e -> e.getError().contains("lehrjahr"))) {
                gehaltInfo.setLehrjahr(vertragsdatenDto.getLehrjahr());
                gehaltInfoService.save(gehaltInfo);
            }
            if (!isNullOrBlank(vertragsdatenDto.getStufe())) {
                KVStufe kvStufe = kvStufeService.findAllByName(vertragsdatenDto.getStufe());
                if (kvStufe != null) {
                    gehaltInfo.setStufe(kvStufe);
                    gehaltInfoService.save(gehaltInfo);
                }
            }
        }
    }

    private GehaltInfoZulage mapToGehaltInfoZulage(GehaltInfo gehaltInfo, String artDerZulage, String zulageInEuro) {
        GehaltInfoZulage gehaltInfoZulage = new GehaltInfoZulage();
        gehaltInfoZulage.setGehaltInfo(gehaltInfo);
        gehaltInfoZulage.setZulageInEuro(BigDecimal.valueOf(Double.parseDouble(zulageInEuro)));
        gehaltInfoZulage.setArtDerZulage(artDerZulage);
        gehaltInfoZulage.setChangedBy(getChangedBy());
        gehaltInfoZulage.setCreatedBy(globalUserHolder.getUsername());
        gehaltInfoZulage.setChangedOn(getLocalDateNow());
        return gehaltInfoZulageService.save(gehaltInfoZulage);
    }

    private void updateGehaltInfoZulage(GehaltInfoZulage gehaltInfoZulage, String zulageInEuro) {
        gehaltInfoZulage.setZulageInEuro(BigDecimal.valueOf(Double.parseDouble(zulageInEuro)));
        gehaltInfoZulage.setChangedBy(getChangedBy());
        gehaltInfoZulage.setChangedOn(getLocalDateNow());
    }

    private void setRestFields(Vertragsdaten vertragsdaten, VertragsdatenDto vertragsdatenDto) {
        if (!isNullOrBlank(vertragsdatenDto.getNotizAllgemein())) {
            vertragsdaten.setNotizAllgemein(vertragsdatenDto.getNotizAllgemein());
        } else {
            vertragsdaten.setNotizAllgemein("");
        }
        if (!isNullOrBlank(vertragsdatenDto.getNotizZusatzvereinbarung())) {
            vertragsdaten.setNotizZusatzvereinbarung(vertragsdatenDto.getNotizZusatzvereinbarung());
        } else {
            vertragsdaten.setNotizZusatzvereinbarung("");
        }
        vertragsdaten.setMobileWorking(vertragsdatenDto.getMobileWorking());
    }

    @Override
    protected boolean executeSingleValidationForSingleObject(Validation<VertragsdatenDto, Vertragsdaten> validation, VertragsdatenDto object, Vertragsdaten validatedObject) {
        if (validation instanceof AbstractValidation<VertragsdatenDto, Vertragsdaten> teilnehmerValidation) {
            if (isTeilnehmer) {
                teilnehmerValidation.setSources(Set.of(TeilnehmerSource.TN_ONBOARDING));
            } else {
                teilnehmerValidation.setSources(new HashSet<>());
            }
            if (teilnehmerValidation.shouldValidationRun()) {
                return teilnehmerValidation.executeValidation(object, validatedObject);
            }
            return true;
        }
        return super.executeSingleValidationForSingleObject(validation, object, validatedObject);
    }
}
