package com.ibosng.validationservice.teilnehmer.validations.imported;

import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStaging;
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

/**
 * Required for VHS
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class NationValidation extends AbstractValidation<TeilnehmerStaging, Teilnehmer> {

    private final LandService landService;

    @Override
    public boolean shouldValidationRun() {
        return getSources().stream().anyMatch(source -> List.of(TeilnehmerSource.VHS, TeilnehmerSource.VHS_EAMS, TeilnehmerSource.MANUAL, TeilnehmerSource.SYNC_SERVICE).contains(source));
    }

    @Override
    public boolean executeValidation(TeilnehmerStaging teilnehmerStaging, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerStaging.getNation())) {
            String[] nationsString = teilnehmerStaging.getNation().split("/");
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
                teilnehmer.addError("nation", "Ungültige Nation angegeben", GlobalUserHolder.IBOSNG_BACKEND);
                return false;
            }
            teilnehmer.setNation(nations);
            return true;
        }
        return true;
    }

}
