package com.ibosng.natifservice.services.impl;

import com.ibosng.dbservice.entities.natif.Kompetenz;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.natif.KompetenzenService;
import com.ibosng.natifservice.dtos.extractions.ExtractionsDto;
import com.ibosng.natifservice.dtos.extractions.kompetenzen.CategorizedKompetenz;
import com.ibosng.natifservice.services.NatifMapperService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static org.springframework.util.NumberUtils.parseNumber;

@Service
@Slf4j
@RequiredArgsConstructor
public class NatifMapperServiceImpl implements NatifMapperService {

    private final KompetenzenService kompetenzenService;
    private final TeilnehmerService teilnehmerService;

    @Override
    public void saveKompetenz(ExtractionsDto extractionsDto, Integer teilnehmerId) {

        List<CategorizedKompetenz> kompetenzArten = getCategorizedKompetenzen(extractionsDto);

        if (kompetenzArten.isEmpty()){
            throw new IllegalStateException("Datei enthält keine Kompetenzen!");
        }

        LocalDate tagesDatum = extractionsDto.getTagesdatum() != null ?
                parseDate(extractionsDto.getTagesdatum().getValue()) :null;

        Double confidenceTagesdatum = extractionsDto.getTagesdatum() != null ?
                extractionsDto.getTagesdatum().getConfidence() : null;

        String pinCode = extractionsDto.getPinCode() != null ?
        extractionsDto.getPinCode().getValue() : null;

        Double confidencePinCode = extractionsDto.getPinCode() != null ?
                extractionsDto.getPinCode().getConfidence() : null;

        List<Kompetenz> existingKompetenzen = kompetenzenService.getKompetenzByTeilnehmerId(teilnehmerId);

        if (!existingKompetenzen.isEmpty()){
            kompetenzenService.deleteByTeilnehmerId(teilnehmerId);
        }

        kompetenzArten.stream()
                .map(k -> mapToKompetenz(k, teilnehmerId))
                .filter(Objects::nonNull)
                .forEach(k -> {

                    //region This might be needed in the future. As of 17.03.2025 Leo wanted to have it so that the kompetenzen are removed and not updated if new ones come in.
//                    List<Kompetenz> existingKompetenzen = kompetenzenService.getKompetenzByTeilnehmerId(teilnehmerId);
//
//                    if (kompetenzenService.isExists(k)) {
//                        Kompetenz existingKompetenz = existingKompetenzen.stream()
//                                .filter(existing -> existing.equals(k))
//                                .findFirst()
//                                .orElse(null);
//
//                        if (existingKompetenz != null && !existingKompetenz.equals(k)) {
//                            k.setId(existingKompetenz.getId());
//                            k.setTagesdatum(tagesDatum);
//                            k.setConfidenceTagesdatum(confidenceTagesdatum);
//                            k.setPinCode(pinCode);
//                            k.setConfidencePinCode(confidencePinCode);
//                            kompetenzenService.save(k);
//                        }
//                    } else {
                    //endregion

                        teilnehmerService.findById(teilnehmerId).ifPresent(
                                teilnehmer -> teilnehmerService.updateHasBisDocument(true, teilnehmerId)
                        );
                        k.setTagesdatum(tagesDatum);
                        k.setConfidenceTagesdatum(confidenceTagesdatum);
                        k.setPinCode(pinCode);
                        k.setConfidencePinCode(confidencePinCode);
                        kompetenzenService.save(k);
//                    }
                });
    }

    private List<CategorizedKompetenz> getCategorizedKompetenzen(ExtractionsDto extractionsDto) {
        return Stream.of(
                        extractionsDto.getFachlich(),
                        extractionsDto.getTeilDerBerufsobergruppe(),
                        extractionsDto.getVoraussetzung(),
                        extractionsDto.getSuchbegriffe(),
                        extractionsDto.getUeberfachlich(),
                        extractionsDto.getInteressengebiete(),
                        extractionsDto.getAmsSechsSteller(),
                        extractionsDto.getZertifikate()
                )
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private Kompetenz mapToKompetenz(CategorizedKompetenz abstrKompetenz, Integer teilnehmerId) {
        Kompetenz kompetenz = new Kompetenz();

        if (abstrKompetenz == null) {
            return null;
        }

        teilnehmerService.findById(teilnehmerId).ifPresent(kompetenz::setTeilnehmer);

        kompetenz.setArt(abstrKompetenz.getArt());

        kompetenz.setName(abstrKompetenz.getKompetenz() != null
                ? abstrKompetenz.getKompetenz().getValue()
                : null);

        kompetenz.setConfidenceName(abstrKompetenz.getKompetenz() != null
                ? abstrKompetenz.getKompetenz().getConfidence()
                : null);

        kompetenz.setScore(abstrKompetenz.getScore() != null
                ? parseNumber(abstrKompetenz.getScore().getValue(), BigDecimal.class)
                : null);

        kompetenz.setConfidenceScore(abstrKompetenz.getScore() != null
                ? abstrKompetenz.getScore().getConfidence()
                : null);
        return kompetenz;
    }
}
