package com.ibosng.dbmapperservice.services.impl;

import com.ibosng.dbmapperservice.services.UrlaubsdatenMapper;
import com.ibosng.dbservice.dtos.urlaubsdaten.UrlaubDto;
import com.ibosng.dbservice.dtos.urlaubsdaten.UrlaubTopLevelDto;
import com.ibosng.dbservice.dtos.urlaubsdaten.UrlaubeListDto;
import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class UrlaubsdatenMapperImpl implements UrlaubsdatenMapper {

    @Override
    public UrlaubeListDto mapToUrlaubeListDto(List<Urlaubsdaten> urlaubsdaten) {
        if (urlaubsdaten == null || urlaubsdaten.isEmpty()) {
            return null;
        }

        urlaubsdaten.sort(Comparator.comparing(o -> o.getAnspruchType().getId()));
        UrlaubTopLevelDto topLevelDto = new UrlaubTopLevelDto();

        if (!urlaubsdaten.isEmpty()) {
            Urlaubsdaten firstEntry = urlaubsdaten.get(0);

            topLevelDto.setAnspruch(firstEntry.getAnspruch());
            topLevelDto.setKonsum(firstEntry.getKonsum());
            topLevelDto.setKuerzung(firstEntry.getKuerzung());
            topLevelDto.setRest(firstEntry.getRest());
            topLevelDto.setVerjaehrung(firstEntry.getVerjaehrung());

            if (firstEntry.getNextAnspruch() != null) {
                topLevelDto.setNextAnspruch(firstEntry.getNextAnspruch().format(DateTimeFormatter.ISO_LOCAL_DATE));
            } else {
                topLevelDto.setNextAnspruch(null);
            }
        }

        List<UrlaubDto> urlaubDtos = new ArrayList<>();

        for (Urlaubsdaten urlaubsdatenItem : urlaubsdaten) {
            UrlaubDto urlaubDto = new UrlaubDto();

            if (urlaubsdatenItem.getAnspruchType() != null) {
                urlaubDto.setAnspuruchType(urlaubsdatenItem.getAnspruchType().getBezeichnung());
            }

            if (urlaubsdatenItem.getMonth() != null) {
                urlaubDto.setMonth(urlaubsdatenItem.getMonth().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }

            if (urlaubsdatenItem.getFrom() != null) {
                urlaubDto.setFromDate(urlaubsdatenItem.getFrom().format(DateTimeFormatter.ISO_LOCAL_DATE));
            }

            urlaubDtos.add(urlaubDto);
        }

        return UrlaubeListDto.builder()
                .overviewData(topLevelDto)
                .urlaubList(urlaubDtos)
                .build();
    }
}
