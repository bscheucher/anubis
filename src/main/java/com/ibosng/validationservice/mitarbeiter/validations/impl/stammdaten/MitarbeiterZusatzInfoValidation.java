package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.ZusatzInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.ZusatzInfoService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.ibosng.validationservice.utils.ValidationHelpers.createNewZusatzInfo;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterZusatzInfoValidation extends AbstractValidation<StammdatenDto, Stammdaten> {
    private final ZusatzInfoService zusatzInfoService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (stammdaten.getZusatzInfo() == null) {
            stammdaten.setZusatzInfo(zusatzInfoService.save(createNewZusatzInfo(globalUserHolder.getUsername())));
        }

        ZusatzInfo zusatzInfo = stammdaten.getZusatzInfo();
        if (stammdatenDto.getWien() != null) {
            zusatzInfo.setWien(stammdatenDto.getWien());
        }
        if (stammdatenDto.getNiederoesterreich() != null) {
            zusatzInfo.setNiederoesterreich(stammdatenDto.getNiederoesterreich());
        }
        if (stammdatenDto.getOberoesterreich() != null) {
            zusatzInfo.setOberoesterreich(stammdatenDto.getOberoesterreich());
        }
        if (stammdatenDto.getSalzburg() != null) {
            zusatzInfo.setSalzburg(stammdatenDto.getSalzburg());
        }
        if (stammdatenDto.getTirol() != null) {
            zusatzInfo.setTirol(stammdatenDto.getTirol());
        }
        if (stammdatenDto.getBurgenland() != null) {
            zusatzInfo.setBurgenland(stammdatenDto.getBurgenland());
        }
        if (stammdatenDto.getSteiermark() != null) {
            zusatzInfo.setSteiermark(stammdatenDto.getSteiermark());
        }
        if (stammdatenDto.getKaernten() != null) {
            zusatzInfo.setKaernten(stammdatenDto.getKaernten());
        }
        if (stammdatenDto.getVorarlberg() != null) {
            zusatzInfo.setVorarlberg(stammdatenDto.getVorarlberg());
        }

        zusatzInfo.setChangedBy(globalUserHolder.getUsername());
        zusatzInfo.setChangedOn(LocalDateTime.now());
        zusatzInfo.setStatus(MitarbeiterStatus.VALIDATED);

        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }
}
