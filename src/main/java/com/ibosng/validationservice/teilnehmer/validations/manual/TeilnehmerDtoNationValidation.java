package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.services.LandService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.ValidationHelpers.capitalizeFirstLetter;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoNationValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final LandService landService;

    private final GlobalUserHolder globalUserHolder;


    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerDto.getNation())) {
            String[] nationsString = teilnehmerDto.getNation().split("/");
            Set<Land> nations = new HashSet<>();
            for (String singleNationString : nationsString) {
                singleNationString = singleNationString.trim();
                Land landEldaCode = landService.findByEldaCode(singleNationString.toUpperCase());
                List<Land> landLandCode = landService.findByLandCode(singleNationString.toUpperCase());
                Land landLandName = landService.findByLandName(capitalizeFirstLetter(singleNationString));
                if (landEldaCode != null) {
                    nations.add(landEldaCode);
                } else if (landLandCode != null && !landLandCode.isEmpty()) {
                    nations.add(landLandCode.get(0));
                } else if (landLandName != null) {
                    nations.add(landLandName);
                }
            }
            if (nations.isEmpty()) {
                teilnehmer.addError("nation", "Ungültige Nation angegeben", globalUserHolder.getUsername());
                return false;
            }
            teilnehmer.setNation(nations);
            return true;
        }
        teilnehmer.addError("nation", "Ungültige Nation angegeben", globalUserHolder.getUsername());
        return false;
    }

}
