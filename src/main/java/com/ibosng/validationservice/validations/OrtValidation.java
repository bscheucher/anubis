package com.ibosng.validationservice.validations;

import com.ibosng.dbservice.services.PlzService;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.validationservice.utils.ValidationHelpers.findMatchingOrts;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class OrtValidation {
    private final PlzService plzService;

    public OrtValidation(PlzService plzService) {
        this.plzService = plzService;
    }


    public String validateOrt(String ortInput) {
        String ort = null;
        if (!isNullOrBlank(ortInput)) {
            if (!ortInput.matches("^[\\p{L},/\\s.\\-]+$")) {
                return ort;
            } else {
                return getValidOrt(plzService.getAllOrt(), ortInput);
            }
        }
        return ort;
    }

    private String getValidOrt(List<String> allOrt, String ortToCheck) {
        String cleanUpOrt = ortToCheck.trim();
        if (!findMatchingOrts(allOrt, cleanUpOrt).isEmpty()) {
            return cleanUpOrt;
        }
        return null;
    }
}
