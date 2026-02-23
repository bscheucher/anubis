package com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude()
public class AuszahlungsanfrageTransferDto {

    private DienstnehmerRef dienstnehmer;
    String anfrage;
    String status;

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
