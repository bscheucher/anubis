package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.SeminarDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.*;

@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SeminarDtoBegehrenBisValidation extends AbstractValidation<SeminarDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(SeminarDto seminarDto, Teilnehmer teilnehmer) {
        Teilnehmer2Seminar teilnehmer2Seminar = teilnehmer.getTeilnehmerSeminars().stream()
                .filter(t2s -> t2s.getSeminar() != null && t2s.getSeminar().getId().equals(seminarDto.getId()))
                .findFirst()
                .orElse(null);

        if (teilnehmer2Seminar != null) {
            if (isNullOrBlank(seminarDto.getBegehrenBis())) {
                teilnehmer2Seminar.setBegehrenBis(null);
                return true;
            }

            if (!isValidDate(seminarDto.getBegehrenBis())) {
                teilnehmer.addError("begehrenBis", "Ungültiges Datum", globalUserHolder.getUsername());
                return false;
            } else {
                teilnehmer2Seminar.setBegehrenBis(parseDate(seminarDto.getBegehrenBis()));
                return true;
            }
        }
        return false;
    }
}
