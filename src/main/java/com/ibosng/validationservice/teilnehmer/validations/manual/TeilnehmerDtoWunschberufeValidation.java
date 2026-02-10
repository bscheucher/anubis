package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.teilnehmer.Beruf;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Wunschberuf;
import com.ibosng.dbservice.services.masterdata.BerufService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoWunschberufeValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;
    private final BerufService berufService;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        Set<String> newWunschberufe = teilnehmerDto.getWunschberufe().stream()
                .map(String::trim)
                .collect(Collectors.toSet());
        List<Teilnehmer2Wunschberuf> currentWunschberufe = teilnehmer.getTeilnehmer2Wunschberufe();

        currentWunschberufe.removeIf(t2w ->
                !newWunschberufe.contains(t2w.getWunschberuf().getName())
        );

        Set<String> alreadyAssigned = currentWunschberufe.stream()
                .map(t2w -> t2w.getWunschberuf().getName())
                .collect(Collectors.toSet());

        for (String name : newWunschberufe) {
            if (!alreadyAssigned.contains(name)) {
                Beruf beruf = berufService.findByName(name);
                if (beruf == null) {
                    Beruf newWunschberuf = new Beruf();
                    newWunschberuf.setName(name);
                    newWunschberuf.setCreatedBy(globalUserHolder.getUsername());
                    newWunschberuf.setCreatedOn(getLocalDateNow());
                    newWunschberuf.setStatus(Status.ACTIVE);
                    beruf = berufService.save(newWunschberuf);
                }

                Teilnehmer2Wunschberuf t2w = new Teilnehmer2Wunschberuf();
                t2w.setTeilnehmer(teilnehmer);
                t2w.setWunschberuf(beruf);
                t2w.setCreatedBy(globalUserHolder.getUsername());
                t2w.setCreatedOn(getLocalDateNow());

                currentWunschberufe.add(t2w);
            }
        }
        return true;
    }
}
