package com.ibosng.dbservice.dtos.zeiterfassung.umbuchung;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@JsonInclude()
@NoArgsConstructor
@AllArgsConstructor
public class Zeitspeicher2ValueDto {
    @NotNull(message = "zspNummer can`t be null")
    private Integer zspNummer;
    private String zspName;
    private String zspComment;
    @NotNull(message = "value can`t be null")
    private Integer value;
    private Integer abrechnung;
    private Integer uebertrag;
}
