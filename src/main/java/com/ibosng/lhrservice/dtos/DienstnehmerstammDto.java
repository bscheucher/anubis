package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.validators.ValidPhoneNumber;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DienstnehmerstammDto {

    private DnAbteilungDto abteilung;
    private AdresseDto adresse;
    private DnBankverbindungDto bankverbindung;
    private GruppeNameNrDto beruf;
    private List<DnErreichbarkeitDto> erreichbarkeiten;
    private DienstnehmerstammZeiterfassungDto zeiterfassung;
    private String familienstand;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate geburtsdatum;
    private String geburtsname;
    private GemeindeDto geburtsort;
    private DnGehaltsinfoDto gehaltsinfo;
    private String geschlecht;
    private String legitimation;
    private String name;
    private DnReligionDto religion;
    private String staatsbuergerschaft;
    private String svNummer;
    @ValidPhoneNumber
    private String telefon;
    private String titel;
    private String titel2;
    private String titelIntern;
    private String vorname;
}
