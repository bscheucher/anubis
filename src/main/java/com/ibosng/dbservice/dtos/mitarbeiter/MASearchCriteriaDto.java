package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Data
@NoArgsConstructor
public class MASearchCriteriaDto {
    private String searchTerm;
    private String wohnort;
    private List<String> firmen;
    private List<String> kostenstellen;
    private List<String> beschaeftigungstatusen;
    private List<String> jobbezeichnungen;
    private List<String> kategorien;

    public MASearchCriteriaDto(String searchTerm,
                               String wohnort,
                               List<String> firmen,
                               List<String> kostenstellen,
                               List<String> beschaeftigungstatusen,
                               List<String> jobbezeichnungen,
                               List<String> kategorien) {
        if (!isNullOrBlank(searchTerm)) {
            this.searchTerm = searchTerm;
        }
        if (!isNullOrBlank(wohnort)) {
            this.wohnort = wohnort;
        }
        if (firmen != null) {
            this.firmen = firmen;
        }
        if (kostenstellen != null) {
            this.kostenstellen = kostenstellen;
        }
        if (beschaeftigungstatusen != null) {
            this.beschaeftigungstatusen = beschaeftigungstatusen;
        }
        if (jobbezeichnungen != null) {
            this.jobbezeichnungen = jobbezeichnungen;
        }
        if (kategorien != null) {
            this.kategorien = kategorien;
        }
    }
}
