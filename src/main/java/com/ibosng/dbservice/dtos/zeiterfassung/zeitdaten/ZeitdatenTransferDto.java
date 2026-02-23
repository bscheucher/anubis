package com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@JsonInclude()
public class ZeitdatenTransferDto {
    private DienstnehmerRef dienstnehmer;
    private String date;
    private boolean fehler;
    private ZeitmodellDto zeitmodell;
    private List<ZeitspeicherWertDto> zeitspeicherWerte;
    private List<BuchungDto> buchungen;
    private List<ZeitdatenAbwesenheitDto> abwesenheiten;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(toBuilder = true)
    private static class DienstnehmerRef {
        private Integer dnNr;
        private String faKz;
        private Integer faNr;
    }
}
