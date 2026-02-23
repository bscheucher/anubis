package com.ibosng.validationservice.zeitbuchung.validations.impl;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.services.masterdata.KostenstelleService;
import com.ibosng.validationservice.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeitbuchungKostenstelleValidation implements Validation<ZeitbuchungenDto, Zeitbuchung> {

    private final KostenstelleService kostenstelleService;

    @Override
    public boolean executeValidation(ZeitbuchungenDto zeitbuchungenDto, Zeitbuchung zeitbuchung) {
        if (zeitbuchungenDto.getKostenstellenummer() != null && (zeitbuchungenDto.getKostenstellenummer() != 0)) {
            Kostenstelle kostenstelle = kostenstelleService.findByNummer(zeitbuchungenDto.getKostenstellenummer());
            zeitbuchung.setKostenstelle(kostenstelle);
            zeitbuchungenDto.setKostenstelle((kostenstelle != null) ? kostenstelle.getBezeichnung() : null);
            return true;
        }
        log.info("Kostenstelle with nummer -'{}' didn`t found", zeitbuchungenDto.getKostenstellenummer());
        return false;
    }
}
