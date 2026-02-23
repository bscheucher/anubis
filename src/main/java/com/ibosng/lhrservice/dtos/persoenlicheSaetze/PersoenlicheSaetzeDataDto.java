package com.ibosng.lhrservice.dtos.persoenlicheSaetze;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class PersoenlicheSaetzeDataDto {
    private int satznummer;
    private String name;
    private double wert;
    private String kommentar;
}
