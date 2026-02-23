package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerStammdatenDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.mitarbeiter.BankDaten;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.StammdatenDataStatus;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.mitarbeiter.BankDatenService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import fr.marcwrobel.jbanking.bic.Bic;
import fr.marcwrobel.jbanking.iban.Iban;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoBankdatenValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;
    private final BankDatenService bankDatenService;
    private final StammdatenService stammdatenService;
    private final LandService landService;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        boolean allValid = true;
        if (teilnehmer != null && teilnehmer.getPersonalnummer() != null) {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerAndStatusIn(teilnehmer.getPersonalnummer(), List.of(MitarbeiterStatus.ACTIVE, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED));
            createBankEntity(stammdaten);
            stammdaten.getErrors().removeIf(status -> {
                String error = status.getError();
                return "iban".equalsIgnoreCase(error) || "bank".equalsIgnoreCase(error)
                        || "bic".equalsIgnoreCase(error) || "bankcard".equalsIgnoreCase(error);
            });
            if (stammdaten.getPersonalnummer() != null && MitarbeiterType.TEILNEHMER.equals(stammdaten.getPersonalnummer().getMitarbeiterType()) && teilnehmerDto.getStammdaten() != null) {
                TeilnehmerStammdatenDto stammdatenDto = teilnehmerDto.getStammdaten();
                if (!isNullOrBlank(stammdatenDto.getBankcard())) {
                    BlobStatus status = BlobStatus.fromValue(stammdatenDto.getBankcard());
                    if (status.equals(BlobStatus.NONE)) {
                        allValid = false;
                        stammdaten.addError("bankcard", "Das Feld ist leer", globalUserHolder.getUsername());
                    } else {
                        stammdaten.getBank().setCard(BlobStatus.fromValue(stammdatenDto.getBankcard()));
                    }
                }

                // IBAN validation
                if (!isNullOrBlank(stammdatenDto.getIban())) {
                    allValid = validateIban(teilnehmerDto, stammdaten, allValid);
                } else {
                    stammdaten.getBank().setIban("");
                    stammdaten.getBank().setLand(null);
                }

                // Bank name & BIC validation: only if the bank is not an austrian one
                boolean isSepa = stammdaten.getBank() != null && stammdaten.getBank().getLand() != null && stammdaten.getBank().getLand().getIsInSepa();
                if (!isNullOrBlank(stammdatenDto.getBank())) {
                    stammdaten.getBank().setBank(stammdatenDto.getBank());
                } else if (isNullOrBlank(stammdatenDto.getBank())) {
                    stammdaten.getBank().setBank("");
                }

                if (!isNullOrBlank(stammdatenDto.getBic())) {
                    allValid = validateBic(teilnehmerDto, stammdaten, allValid);
                } else if (isNullOrBlank(stammdatenDto.getBic())) {
                    stammdaten.getBank().setBic("");
                }

                if (!isNullOrBlank(stammdatenDto.getIban())) {
                    if (isNullOrBlank(stammdatenDto.getBankcard())) {
                        allValid = false;
                        stammdaten.addError("bankcard", "Feld erforderlich bei Angabe des IBANs", globalUserHolder.getUsername());
                    }
                    if (!isSepa) {
                        if (isNullOrBlank(stammdatenDto.getBic())) {
                            allValid = false;
                            stammdaten.addError("bic", "Feld erforderlich für IBANs außerhalb Österreichs", globalUserHolder.getUsername());
                        }
                        if (isNullOrBlank(stammdatenDto.getBank())) {
                            allValid = false;
                            stammdaten.addError("bank", "Feld erforderlich für IBANs außerhalb Österreichs", globalUserHolder.getUsername());
                        }
                    }
                } else if (isNullOrBlank(stammdatenDto.getIban()) && (!isNullOrBlank(stammdatenDto.getBank()) || !isNullOrBlank(stammdatenDto.getBankcard()) || !isNullOrBlank(stammdatenDto.getBic()))) {
                    allValid = false;
                    stammdaten.addError("iban", "Feld erforderlich", globalUserHolder.getUsername());
                }
            }
            teilnehmerDto.getStammdaten().getErrorsMap().clear();
            for (StammdatenDataStatus dataStatus : stammdaten.getErrors()) {
                String error = dataStatus.getError();
                if ("iban".equalsIgnoreCase(error) || "bank".equalsIgnoreCase(error)
                        || "bic".equalsIgnoreCase(error) || "bankcard".equalsIgnoreCase(error)) {
                    teilnehmerDto.getStammdaten().getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
                }
            }
            teilnehmerDto.getStammdaten().setErrors(new ArrayList<>(teilnehmerDto.getStammdaten().getErrorsMap().keySet()));
        }
        return allValid;
    }

    private boolean validateBic(TeilnehmerDto teilnehmerDto, Stammdaten stammdaten, boolean allValid) {
        try {
            TeilnehmerStammdatenDto stammdatenDto = teilnehmerDto.getStammdaten();
            new Bic(stammdatenDto.getBic().replaceAll("\\s+", ""));
            stammdaten.getBank().setBic(stammdatenDto.getBic());
        } catch (IllegalArgumentException e) {
            log.warn("Invalid bic for teilnehmer: {}, {}, {}", teilnehmerDto.getVorname(), " ", teilnehmerDto.getNachname());
            stammdaten.addError("bic", "Ungültiger BIC", globalUserHolder.getUsername());
            allValid = false;
        }
        return allValid;
    }

    private boolean validateIban(TeilnehmerDto teilnehmerDto, Stammdaten stammdaten, boolean allValid) {
        try {
            TeilnehmerStammdatenDto stammdatenDto = teilnehmerDto.getStammdaten();
            String ibanString = stammdatenDto.getIban().replaceAll("\\s+", "");
            Iban iban = new Iban(ibanString);
            Land land = findLand(iban.getCountry().getAlpha2Code());
            if (land != null) {
                stammdaten.getBank().setLand(land);
            }
            if (!isNullOrBlank(iban.getBankIdentifier())) {
                stammdaten.getBank().setBlz(iban.getBankIdentifier());
            }
            stammdaten.getBank().setIban(ibanString);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid iban for teilnehmer: {}, {}, {}", teilnehmerDto.getVorname(), " ", teilnehmerDto.getNachname());
            stammdaten.addError("iban", "Ungültige IBAN", globalUserHolder.getUsername());
            allValid = false;
        }
        return allValid;
    }

    private Land findLand(String landCode) {
        List<Land> laender = landService.findByLandCode(landCode);
        if (laender.size() > 1) {
            log.warn("Multiple laender found for country code {}", landCode);
        } else if (laender.isEmpty()) {
            return null;
        }
        return laender.get(0);
    }

    private void createBankEntity(Stammdaten stammdaten) {
        if (stammdaten.getBank() == null) {
            BankDaten bankDaten = new BankDaten();
            bankDaten.setStatus(MitarbeiterStatus.NEW);
            bankDaten.setCreatedBy(globalUserHolder.getUsername());
            bankDaten.setCreatedOn(getLocalDateNow());
            bankDaten = bankDatenService.save(bankDaten);
            stammdaten.setBank(bankDaten);
        }

        BankDaten stammdatenBank = stammdaten.getBank();

        if (!MitarbeiterStatus.NEW.equals(stammdatenBank.getStatus())) {
            stammdatenBank.setStatus(MitarbeiterStatus.INACTIVE);
        } else {
            stammdatenBank.setStatus(MitarbeiterStatus.VALIDATED);
        }

        stammdatenBank.setChangedBy(globalUserHolder.getUsername());
        stammdatenBank.setChangedOn(getLocalDateNow());
        stammdaten.setBank(bankDatenService.save(stammdatenBank));
    }
}
