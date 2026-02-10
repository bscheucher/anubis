package com.ibosng.dbservice.dtos.teilnehmer;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude()
public class TeilnehmerDto {
    private int id;
    private String titel;
    private String titel2;
    private String nachname;
    private String vorname;
    private String geschlecht;
    private String svNummer;
    private String telefon;
    private String geburtsdatum;
    private String buchungsstatus;
    private String anmerkung;
    private String geplant;
    private String eintritt;
    private String austritt;
    private String massnahmennummer;
    private String veranstaltungsnummer;
    private String email;
    private String zubuchung;
    private String rgs;
    private String rgsBezeichnung;
    private String nation;
    private String plz;
    private String ort;
    private String land;
    private String strasse;
    private String betreuerTitel;
    private String betreuerVorname;
    private String betreuerNachname;
    private String seminarBezeichnung;
    private Integer seminarNumber;
    private List<String> errors = new ArrayList<>();
    private Map<String, String> errorsMap = new HashMap<>();
    private int source;
    private String anrede;
    private boolean isUeba;
    private String status;
    private String personalnummer;
    private String naechsteVorrueckung;
    private boolean hasBisDocument;
    private String ursprungsland;
    private String geburtsort;
    private String ziel;
    private String vermittelbarAb;
    private String vermittlungsnotiz;
    private boolean vermittelbarAusserhalbAms;
    private List<String> wunschberufe = new ArrayList<>();
    private List<String> kompetenzen;
    private TeilnehmerStammdatenDto stammdaten;
    private TeilnehmerVertragsdatenDto vertragsdaten;
    private String muttersprache;
    // laut #13695 nicht nutzen, braucht noch Analyse
   /* private List<TnPraktikaDto> praktika;
    private List<TnAbschlussDto> abschluesse;*/
}
