package com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class ZeitdatenOptionDto {
    private int id;
    private String bezeichnung;
    private boolean aktiv;
}
