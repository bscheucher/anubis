package com.ibosng.lhrservice.dtos.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten.BuchungDto;
import com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten.ZeitdatenAbwesenheitDto;
import com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten.ZeitmodellDto;
import com.ibosng.dbservice.dtos.zeiterfassung.zeitdaten.ZeitspeicherWertDto;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class DnZeitdatenDto {
    private DienstnehmerRefDto dienstnehmer;
    private String date;
    private boolean fehler;
    private ZeitmodellDto zeitmodell;
    private List<ZeitspeicherWertDto> zeitspeicherWerte;
    private List<BuchungDto> buchungen;
    private List<ZeitdatenAbwesenheitDto> abwesenheiten;
}
