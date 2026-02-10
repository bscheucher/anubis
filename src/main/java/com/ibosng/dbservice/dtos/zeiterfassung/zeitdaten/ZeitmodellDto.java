package com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class ZeitmodellDto {
    private int nummer;
    private String name;
    private List<ZeitdatenOptionDto> optionen;
}
