package com.ibosng.lhrservice.dtos.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class ZeitdatenStatusStatusDto {
    private String fertigmeldung;
    private String freigabe;
    private String abrechnung;
    private String auszahlung;
}
