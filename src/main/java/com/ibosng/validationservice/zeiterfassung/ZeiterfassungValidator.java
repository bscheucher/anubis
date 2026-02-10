package com.ibosng.validationservice.zeiterfassung;

import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.BaseService;
import com.ibosng.validationservice.SingleEntityAbstractValidator;
import com.ibosng.validationservice.ValidationObjectPair;
import com.ibosng.validationservice.zeiterfassung.validations.impl.ZeiterfassungPeriodValidation;
import com.ibosng.validationservice.zeiterfassung.validations.impl.ZeiterfassungSeminarValidation;
import com.ibosng.validationservice.zeiterfassung.validations.impl.ZeiterfassungTeilnehmerValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeiterfassungValidator extends SingleEntityAbstractValidator<ZeiterfassungTransferDto, ZeiterfassungTransfer> {
    private final ZeiterfassungPeriodValidation zeiterfassungPeriodValidation;
    private final ZeiterfassungSeminarValidation zeiterfassungSeminarValidation;
    private final ZeiterfassungTeilnehmerValidation zeiterfassungTeilnehmerValidation;

    public ZeiterfassungValidator(BaseService<ZeiterfassungTransfer> baseServiceV,
                                  ZeiterfassungPeriodValidation zeiterfassungPeriodValidation,
                                  ZeiterfassungSeminarValidation zeiterfassungSeminarValidation,
                                  ZeiterfassungTeilnehmerValidation zeiterfassungTeilnehmerValidation) {
        super(baseServiceV);
        this.zeiterfassungPeriodValidation = zeiterfassungPeriodValidation;
        this.zeiterfassungSeminarValidation = zeiterfassungSeminarValidation;
        this.zeiterfassungTeilnehmerValidation = zeiterfassungTeilnehmerValidation;
    }

    @Override
    protected void prepare() {
        super.prepare();

        zeiterfassungTeilnehmerValidation.setCreatedBy(getChangedBy());

        addValidation(zeiterfassungPeriodValidation);
        addValidation(zeiterfassungSeminarValidation);
        addValidation(zeiterfassungTeilnehmerValidation);
    }

    @Override
    protected void executeValidations() {
        for (ZeiterfassungTransferDto object : this.objectsToValidate) {
            ZeiterfassungTransfer validatedObject = findZeiterfassungTransfer(object.getId());
            boolean validationsResult = executeValidationsForOneObject(object, validatedObject);
            validationResults.put(new ValidationObjectPair<>(object, validatedObject), validationsResult);
        }
    }

    @Override
    protected List<ZeiterfassungTransferDto> postProcess() {
        List<ZeiterfassungTransferDto> zeiterfassungDtos = new ArrayList<>();
        for (Map.Entry<ValidationObjectPair<ZeiterfassungTransferDto, ZeiterfassungTransfer>, Boolean> entry : validationResults.entrySet()) {
            ZeiterfassungTransferDto zeiterfassungDto = entry.getKey().getFirst();
            ZeiterfassungTransfer zeiterfassung = entry.getKey().getSecond();
            if (entry.getValue()) {
                setStatusAndSaveZeiterfassung(zeiterfassungDto, zeiterfassung, ZeiterfassungStatus.VALID);
            } else {
                setStatusAndSaveZeiterfassung(zeiterfassungDto, zeiterfassung, ZeiterfassungStatus.INVALID);
            }
            zeiterfassungDtos.add(zeiterfassungDto);
        }

        return zeiterfassungDtos;
    }

    private void setStatusAndSaveZeiterfassung(ZeiterfassungTransferDto zeiterfassungDto, ZeiterfassungTransfer zeiterfassungTransfer, ZeiterfassungStatus status) {
        log.info("Saving ZeiterfassungTransfer with id {} and status {}", zeiterfassungTransfer.getId(), status);
        zeiterfassungTransfer.setStatus(status);
        zeiterfassungTransfer.setChangedBy(getChangedBy());
        zeiterfassungTransfer.setChangedOn(getLocalDateNow());
        zeiterfassungTransfer.setCreatedBy(getChangedBy());
        zeiterfassungTransfer = baseServiceV.save(zeiterfassungTransfer);
        zeiterfassungDto.setId(zeiterfassungTransfer.getId());
    }

    private ZeiterfassungTransfer findZeiterfassungTransfer(Integer id) {
        Optional<ZeiterfassungTransfer> zeiterfassungTransferOptional = baseServiceV.findById(id);
        ZeiterfassungTransfer zeiterfassungTransfer;
        if(zeiterfassungTransferOptional.isPresent()) {
            zeiterfassungTransfer = zeiterfassungTransferOptional.get();
            zeiterfassungTransfer.setStatus(ZeiterfassungStatus.IN_PROGRESS);
        } else {
            zeiterfassungTransfer = new ZeiterfassungTransfer();
            zeiterfassungTransfer.setStatus(ZeiterfassungStatus.NEW);
        }
        zeiterfassungTransfer.setCreatedBy(getChangedBy());
        return baseServiceV.save(zeiterfassungTransfer);
    }
}
