package com.ibosng.dbservice.utils;

import com.ibosng.dbservice.dtos.ReportParameterDto;
import com.ibosng.dbservice.dtos.VereinbarungDto;
import com.ibosng.dbservice.dtos.VereinbarungParameterDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerSeminarDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungCsvDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungXlsxDto;
import com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten.ZeitspeicherDataDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungReportParameter;
import com.ibosng.dbservice.entities.reports.ReportParamType;
import com.ibosng.dbservice.entities.zeiterfassung.Zeitspeicher;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class Mappers {

    public static PruefungCsvDto mapTeilnehmerSeminarDtoToCsvDto(TeilnehmerSeminarDto teilnehmerSeminarDto) {
        if (teilnehmerSeminarDto == null || teilnehmerSeminarDto.getTeilnehmerDto() == null) {
            return null;
        }
        TeilnehmerDto teilnehmer = teilnehmerSeminarDto.getTeilnehmerDto();
        PruefungCsvDto csvDto = new PruefungCsvDto();
        csvDto.setGeschlecht(
                teilnehmer.getGeschlecht() != null && !teilnehmer.getGeschlecht().isEmpty()
                        ? String.valueOf(teilnehmer.getGeschlecht().charAt(0))
                        : ""
        );
        csvDto.setNachname(teilnehmer.getNachname());
        csvDto.setVorname(teilnehmer.getVorname());
        csvDto.setStrasse(teilnehmer.getStrasse());
        csvDto.setPlz(teilnehmer.getPlz());
        csvDto.setOrt(teilnehmer.getOrt());
        csvDto.setGeburtsdatum(teilnehmer.getGeburtsdatum());
        csvDto.setTelefon(teilnehmer.getTelefon());
        csvDto.setEmail(teilnehmer.getEmail());
        csvDto.setStaat(teilnehmerSeminarDto.getNationPalKz());               // "staat" = nation
        csvDto.setHerkunft(teilnehmerSeminarDto.getUrsprungsLandPalKz());     // "herkunft" = ursprungsland
        csvDto.setArt("Sonstiger Prüfungszweck");
        csvDto.setAbrechnung("Institut");

        // Set niveau and seminar for export file name
        csvDto.setNiveauAndSeminar(teilnehmerSeminarDto.getPruefungNiveau() + teilnehmerSeminarDto.getSeminarDtos().get(0).getSeminarBezeichnung());
        return csvDto;
    }

    public static PruefungXlsxDto mapTeilnehmerSeminarDtoToXlsxDto(TeilnehmerSeminarDto teilnehmerSeminarDto) {
        if (teilnehmerSeminarDto == null || teilnehmerSeminarDto.getTeilnehmerDto() == null) {
            return null;
        }
        DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String mappedStufe = "";
        if (teilnehmerSeminarDto.getPruefungNiveau() != null) {
            mappedStufe = switch (teilnehmerSeminarDto.getPruefungNiveau()) {
                case "A1" -> "ZA1/ömod";
                case "B2" -> "ZB2/ömod";
                default -> teilnehmerSeminarDto.getPruefungNiveau(); // fallback or null/empty
            };
        }

        TeilnehmerDto teilnehmer = teilnehmerSeminarDto.getTeilnehmerDto();

        PruefungXlsxDto dto = new PruefungXlsxDto();
        dto.setPruefungsstufe(mappedStufe);
        dto.setTitelVor(teilnehmer.getTitel());
        dto.setVorname(teilnehmer.getVorname());
        dto.setFamilienname(teilnehmer.getNachname());
        dto.setTitelNach(teilnehmer.getTitel2());
        dto.setGeschlecht(teilnehmer.getGeschlecht().toLowerCase());
        dto.setGeburtsOrt(teilnehmer.getGeburtsort());
        dto.setGeburtsLand(teilnehmer.getUrsprungsland());
        dto.setNationalitaet(teilnehmer.getNation());
        if (teilnehmer.getGeburtsdatum() != null) {
            dto.setGeburtsDatum(LocalDate.parse(teilnehmer.getGeburtsdatum(), INPUT_FORMAT).format(OUTPUT_FORMAT));
        }
        // Set niveau and seminar for export file name
        dto.setNiveauAndSeminar(teilnehmerSeminarDto.getPruefungNiveau() + teilnehmerSeminarDto.getSeminarDtos().get(0).getSeminarBezeichnung());

        return dto;
    }

    public static VereinbarungParameterDto mapVereinbarungParameterToDto(VereinbarungReportParameter vereinbarungReportParameter) {
        if (vereinbarungReportParameter == null) {
            return null;
        }

        return new VereinbarungParameterDto(
                vereinbarungReportParameter.getId(),
                vereinbarungReportParameter.getVereinbarung() != null ? vereinbarungReportParameter.getVereinbarung().getId() : null,
                vereinbarungReportParameter.getName(),
                vereinbarungReportParameter.getValue(),
                vereinbarungReportParameter.getType()
        );
    }

    public static VereinbarungDto mapVereinbarungToDto(Vereinbarung vereinbarung, Stammdaten stammdaten) {
        if (vereinbarung == null) {
            return null;
        }

        VereinbarungDto dto = new VereinbarungDto();
        dto.setId(vereinbarung.getId());
        dto.setVereinbarungName(vereinbarung.getVereinbarungName());
        dto.setFirmaName(vereinbarung.getFirma() != null ? vereinbarung.getFirma().getName() : null);
        dto.setPersonalnummer(vereinbarung.getPersonalnummer() != null ? vereinbarung.getPersonalnummer().getPersonalnummer() : null);
        dto.setNachname(stammdaten != null ? stammdaten.getNachname() : null);
        dto.setVorname(stammdaten != null ? stammdaten.getVorname() : null);
        dto.setFuehrungskraft(vereinbarung.getFuehrungskraft());
        dto.setStatus(vereinbarung.getStatus());
        dto.setGueltigAb(vereinbarung.getGueltigAb());
        dto.setGueltigBis(vereinbarung.getGueltigBis());
        dto.setCreatedOn(vereinbarung.getCreatedOn());
        dto.setCreatedBy(vereinbarung.getCreatedBy());
        dto.setChangedOn(vereinbarung.getChangedOn());
        dto.setChangedBy(vereinbarung.getChangedBy());
        dto.setWorkflowId(vereinbarung.getWorkflow().getId());

        // Map parameters if available
        if (vereinbarung.getParameters() != null) {
            dto.setParameters(vereinbarung.getParameters().stream()
                    .map(Mappers::mapVereinbarungParameterToDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setParameters(null);
        }

        return dto;
    }

    public static ZeitspeicherDataDto toZeitspeicherDataDto(Zeitspeicher zeitspeicher) {
        if (zeitspeicher == null) {
            return null;
        }
        ZeitspeicherDataDto dto = new ZeitspeicherDataDto();
        dto.setZeitspeicherNummer(zeitspeicher.getZeitspeicherNummer());
        dto.setName(zeitspeicher.getName());
        dto.setAbbreviation(zeitspeicher.getAbbreviation());
        dto.setComment(zeitspeicher.getComment());
        return dto;
    }

    public static Object mapReportParameter(ReportParameterDto parameterDto) {
        if (parameterDto == null || parameterDto.getValue() == null) {
            throw new IllegalArgumentException("ReportParameterDto or its value cannot be null");
        }

        String value = parameterDto.getValue();
        ReportParamType type = parameterDto.getType();

        switch (type) {
            case STRING:
                return value;
            case DATE:
                try {
                    return Date.valueOf(value); // Converts a string in "yyyy-[m]m-[d]d" format to java.sql.Date
                } catch (IllegalArgumentException e) {
                    log.error("Invalid DATE format for value: {}", value);
                    return null;
                }
            case BOOLEAN:
                return Boolean.parseBoolean(value); // Converts "true" or "false" (case-insensitive)
            case INTEGER:
                try {
                    return Integer.valueOf(value); // Converts string to Integer
                } catch (NumberFormatException e) {
                    log.error("Invalid INTEGER format for value: {}", value);
                    return null;
                }
            case DECIMAL:
                try {
                    return new BigDecimal(value); // Converts string to BigDecimal
                } catch (NumberFormatException e) {
                    log.error("Invalid DECIMAL format for value: {}", value);
                    return null;
                }
            case TIME:
                try {
                    return Time.valueOf(value); // Converts a string in "hh:mm:ss" format to java.sql.Time
                } catch (IllegalArgumentException e) {
                    log.error("Invalid TIME format for value: {}", value);
                    return null;
                }
            default:
                log.error("Unsupported ReportParamType: {}", type);
                return null;
        }
    }

    public static final Map<String, String> DOCUMENT_TO_SUBDIRECTORY = Map.ofEntries(
            Map.entry("Nettozettel", "Gehaltsnachweise/Nettozettel"),
            Map.entry("L16", "Gehaltsnachweise/L16"),
            Map.entry("Anmeldebestätigung (ELDA)", "Sozialversicherungsdaten, ELDA"),
            Map.entry("Abmeldebestätigung (ELDA)", "Sozialversicherungsdaten, ELDA"),
            Map.entry("Bestätigungen (ELDA)", "Sozialversicherungsdaten, ELDA"),
            Map.entry("ecard", "Stammdaten/Ausweise"),
            Map.entry("bankcard", "Stammdaten/Ausweise"),
            Map.entry("foto", "Stammdaten/Ausweise"),
            Map.entry("arbeitsgenehmigung", "Stammdaten/Aufenthaltstitel"),
            Map.entry("dienstvertrag", "Vertrag laufendes DV/Dienstvertrag und Zusätze"),
            Map.entry("vordienstzeit", "Onboarding/Beschäftigungs- & Stundennachweise"),
            Map.entry("zusatzvereinbarungen", "Zusatzvereinbarungen")
    );

    public static String getSubdirectoryForDocument(String documentType) {
        if (!DOCUMENT_TO_SUBDIRECTORY.containsKey(documentType)) {
            log.error("No mapping found for document type: {}", documentType);
        }
        return DOCUMENT_TO_SUBDIRECTORY.get(documentType);
    }
}