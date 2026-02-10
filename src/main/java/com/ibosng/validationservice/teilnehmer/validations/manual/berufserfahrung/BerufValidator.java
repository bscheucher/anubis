package com.ibosng.validationservice.teilnehmer.validations.manual.berufserfahrung;

import com.ibosng.dbservice.dtos.TnBerufserfahrungDto;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.teilnehmer.Beruf;
import com.ibosng.dbservice.entities.teilnehmer.TnBerufserfahrung;
import com.ibosng.dbservice.services.TeilnehmerBerufserfahrungService;
import com.ibosng.dbservice.services.masterdata.BerufService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.manual.FieldValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@RequiredArgsConstructor
public class BerufValidator implements FieldValidator<TnBerufserfahrungDto> {

    private final TeilnehmerBerufserfahrungService teilnehmerBerufserfahrungService;
    private final BerufService berufService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public void validate(TnBerufserfahrungDto dto, Integer parentId) {
        if (isNullOrBlank(dto.getBeruf())) return;
        Beruf beruf = berufService.findByName(dto.getBeruf());
        if (beruf == null) {
            beruf = new Beruf();
            beruf.setName(dto.getBeruf());
            beruf.setCreatedBy(globalUserHolder.getUsername());
            beruf.setCreatedOn(getLocalDateNow());
            beruf.setStatus(Status.ACTIVE);
            beruf = berufService.save(beruf);
        }

        TnBerufserfahrung berufserfahrung = teilnehmerBerufserfahrungService.findByTeilnehmerIdAndBeruf(parentId, beruf.getId());
        if (dto.getId() == null) {
            if (berufserfahrung != null) {
                dto.getErrorsMap().put("beruf", "Beruf existiert bereits");
            }
        } else {
            if (berufserfahrung != null && !berufserfahrung.getId().equals(dto.getId())) {
                dto.getErrorsMap().put("beruf", "Beruf existiert bereits");
            }
        }
    }
}