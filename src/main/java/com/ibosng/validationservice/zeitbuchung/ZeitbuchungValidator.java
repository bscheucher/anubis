package com.ibosng.validationservice.zeitbuchung;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.services.BaseService;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng.validationservice.zeitbuchung.validations.impl.ZeitbuchungAbwesenheitValidation;
import com.ibosng.validationservice.zeitbuchung.validations.impl.ZeitbuchungKostenstelleValidation;
import com.ibosng.validationservice.zeitbuchung.validations.impl.ZeitbuchungLeistungserfassungValidation;
import com.ibosng.validationservice.zeitbuchung.validations.impl.ZeitbuchungLeistungsortValidation;
import com.ibosng.validationservice.zeitbuchung.validations.impl.ZeitbuchungSeminarValidation;
import com.ibosng.validationservice.zeitbuchung.validations.impl.ZeitbuchungTimeValidation;
import com.ibosng.validationservice.zeitbuchung.validations.impl.ZeitbuchungZeitbuchuntypValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeitbuchungValidator extends SingleEntityAbstractValidator<ZeitbuchungenDto, Zeitbuchung> {

    private final ZeitbuchungAbwesenheitValidation zeitbuchungAbwesenheitValidation;
    private final ZeitbuchungKostenstelleValidation zeitbuchungKostenstelleValidation;
    private final ZeitbuchungLeistungserfassungValidation zeitbuchungLeistungserfassungValidation;
    private final ZeitbuchungSeminarValidation zeitbuchungSeminarValidation;
    private final ZeitbuchungTimeValidation zeitbuchungTimeValidation;
    private final ZeitbuchungZeitbuchuntypValidation zeitbuchungZeitbuchuntypValidation;
    private final ZeitbuchungLeistungsortValidation zeitbuchungLeistungsortValidation;

    private final ZeitbuchungService zeitbuchungService;

    public ZeitbuchungValidator(BaseService<Zeitbuchung> baseServiceV, ZeitbuchungAbwesenheitValidation zeitbuchungAbwesenheitValidation, ZeitbuchungKostenstelleValidation zeitbuchungKostenstelleValidation, ZeitbuchungLeistungserfassungValidation zeitbuchungLeistungserfassungValidation, ZeitbuchungSeminarValidation zeitbuchungSeminarValidation, ZeitbuchungTimeValidation zeitbuchungTimeValidation, ZeitbuchungZeitbuchuntypValidation zeitbuchungZeitbuchuntypValidation, ZeitbuchungLeistungsortValidation zeitbuchungLeistungsortValidation, ZeitbuchungService zeitbuchungService) {
        super(baseServiceV);
        this.zeitbuchungAbwesenheitValidation = zeitbuchungAbwesenheitValidation;
        this.zeitbuchungKostenstelleValidation = zeitbuchungKostenstelleValidation;
        this.zeitbuchungLeistungserfassungValidation = zeitbuchungLeistungserfassungValidation;
        this.zeitbuchungSeminarValidation = zeitbuchungSeminarValidation;
        this.zeitbuchungTimeValidation = zeitbuchungTimeValidation;
        this.zeitbuchungZeitbuchuntypValidation = zeitbuchungZeitbuchuntypValidation;
        this.zeitbuchungLeistungsortValidation = zeitbuchungLeistungsortValidation;
        this.zeitbuchungService = zeitbuchungService;
    }

    @Override
    protected void prepare() {
        super.prepare();
        addValidation(zeitbuchungAbwesenheitValidation);
        addValidation(zeitbuchungKostenstelleValidation);
        addValidation(zeitbuchungLeistungserfassungValidation);
        addValidation(zeitbuchungLeistungsortValidation);
        addValidation(zeitbuchungSeminarValidation);
        addValidation(zeitbuchungTimeValidation);
        addValidation(zeitbuchungZeitbuchuntypValidation);
    }

    @Override
    protected void executeValidations() {
        for (ZeitbuchungenDto object : this.objectsToValidate) {
            Zeitbuchung validatedObject = new Zeitbuchung();
            boolean validationsResult = executeValidationsForOneObject(object, validatedObject);
            validationResults.put(new ValidationObjectPair<>(object, validatedObject), validationsResult);
        }
    }

    @Override
    protected List<ZeitbuchungenDto> postProcess() {
        List<ZeitbuchungenDto> zeiterfassungDtos = new ArrayList<>();
        for (Map.Entry<ValidationObjectPair<ZeitbuchungenDto, Zeitbuchung>, Boolean> entry : validationResults.entrySet()) {
            ZeitbuchungenDto zeitbuchungenDto = entry.getKey().getFirst();
            Zeitbuchung zeitbuchung = entry.getKey().getSecond();
            setDataAndSaveZeitbuchung(zeitbuchungenDto, zeitbuchung);
            zeiterfassungDtos.add(zeitbuchungenDto);
        }
        return zeiterfassungDtos;
    }

    private void setDataAndSaveZeitbuchung(ZeitbuchungenDto zeitbuchungenDto, Zeitbuchung zeitbuchung) {
        zeitbuchung.setChangedBy(getChangedBy());
        zeitbuchung.setChangedOn(getLocalDateNow());
        zeitbuchung.setCreatedBy(getChangedBy());
        zeitbuchung.setDauerStd(zeitbuchungenDto.getDauerStd());
        if (!zeitbuchungService.isZeitbuchungExists(
                zeitbuchung.getVon(), zeitbuchung.getBis(), zeitbuchung.getAnAbwesenheit(), zeitbuchung.getLeistungsort(),
                (zeitbuchung.getSeminar() != null) ? zeitbuchung.getSeminar().getId() : null,
                (zeitbuchung.getZeitbuchungstyp() != null) ? zeitbuchung.getZeitbuchungstyp().getId() : null,
                (zeitbuchung.getLeistungserfassung() != null) ? zeitbuchung.getLeistungserfassung().getId() : null,
                (zeitbuchung.getKostenstelle() != null) ? zeitbuchung.getKostenstelle().getId() : null
        )) {
            baseServiceV.save(zeitbuchung);
        }
    }

}