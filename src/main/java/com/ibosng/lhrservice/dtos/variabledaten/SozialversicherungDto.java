package com.ibosng.lhrservice.dtos.variabledaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude()
public class SozialversicherungDto {
    private Integer firmenVersicherungsNr;
    private String firmenVersicherungsName;
    private String beschaeftigtenGruppe;
    private String beschaeftigtenGruppeName;
    private String basisBeschaeftigtenGruppe;
    private String basisBeschaeftigtenGruppeName;
    private Integer svPflichtigkeitNr;
    private String svPflichtigkeitName;
    private Integer gesetzlicheGrundlageNr;
    private String gesetzlicheGrundlageName;
    private Boolean fallweiseBeschaeftigt;
    private Integer anzahlSonderzahlungen;
    private String bundeslandName;
    private Integer dienstortAbteilungNr;
    private String dienstortAbteilungName;
    private String abfertigungsart;
}
