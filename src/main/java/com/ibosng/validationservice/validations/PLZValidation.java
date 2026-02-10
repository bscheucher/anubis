package com.ibosng.validationservice.validations;

import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.services.PlzService;
import com.ibosng.validationservice.utils.Parsers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class PLZValidation {

    private final PlzService plzService;

    public Plz validatePlz(String inputPLZ) {
        Plz plz = null;
        if (!isNullOrBlank(inputPLZ)) {
            if (!inputPLZ.matches("^\\d+$")) {
                return plz;
            } else {
                Integer plzInt = Parsers.parseStringToInteger(inputPLZ);
                List<Plz> allPlz = plzService.findByPlz(plzInt);

                if (!allPlz.isEmpty()) {
                    return allPlz.stream().findFirst().get();
                }
                return plz;
            }
        }
        return plz;
    }
}