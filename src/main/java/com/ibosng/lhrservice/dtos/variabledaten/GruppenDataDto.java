package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;

import static com.ibosng.lhrservice.utils.Constants.LOCAL_DATE_FORMAT;

@Data
@JsonInclude()
public class GruppenDataDto {
    private DienstnehmergruppeDto dnGruppe;
    private AbrechnungsgruppeDto batchGruppe;
    private PflichtigkeitenDto pflichtigkeiten;
    private LohnsteuerDto lohnsteuer;
    private SozialversicherungDto sozialVersicherung;
    private KollektivvertragDto kollektivvertrag;
    private BuchhaltungDto buchhaltung;
    private SonstigesDto sonstiges;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = LOCAL_DATE_FORMAT)
    private LocalDate validFrom;
}
