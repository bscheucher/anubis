package com.ibosng.validationservice.validations;

import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import com.ibosng.dbservice.services.masterdata.GeschlechtService;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.validationservice.utils.ValidationHelpers.capitalizeFirstLetter;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
public class GeschlechtValidation {

    private final GeschlechtService geschlechtService;

    public GeschlechtValidation(GeschlechtService geschlechtService) {
        this.geschlechtService = geschlechtService;
    }

    public Geschlecht getGeschlecht(String geschlechtString) {
        Geschlecht geschlecht = null;
        if (!isNullOrBlank(geschlechtString)) {
            geschlecht = geschlechtService.findByName(capitalizeFirstLetter(geschlechtString));
            if(geschlecht == null) {
                geschlecht = geschlechtService.findByAbbreviation(geschlechtString.toUpperCase());
            }
            if(geschlecht == null) {
                List<Geschlecht> dbGeschlechte = geschlechtService.findAll();
                for(Geschlecht dbGeschlecht : dbGeschlechte) {
                    if(dbGeschlecht.getName().equalsIgnoreCase(geschlechtString)) {
                        geschlecht = dbGeschlecht;
                    }
                }
            }
        }
        return geschlecht;
    }
}
