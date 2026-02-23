package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MitarbeiterDto extends VertragsdatenBasicDto {
    private String personalnummer;
    private String kostenstelle;
    private String firma;
    private String anrede;
    private String titel;
    private String titel2;
    private String nachname;
    private String vorname;
    private String geburtsname;
    private String svnr;
    private String ecard;
    private String geschlecht;
    private String familienstand;
    private String geburtsDatum;
    private String alter;
    private String staatsbuergerschaft;
    private String muttersprache;
    private String strasse;
    private String land;
    private String plz;
    private String ort;
    private String aStrasse;
    private String aLand;
    private String aPlz;
    private String aOrt;
    private String email;
    private String mobilnummer;
    private Boolean handySignatur;
    private String bank;
    private String iban;
    private String bic;
    private String bankcard;
    private String gueltigBis;
    private String arbeitsgenehmigung;

}
