package com.ibosng.lhrservice.dtos.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class ZeitspeicherwertDto {
    private Integer zeitspeicherNummer;
    private String zeitspeicherName;
    private Integer value;
    private String unit;
}
