package com.ibosng.dbibosservice.entities;


import com.ibosng.dbibosservice.utils.YesNoConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Table(name = "SEMINAR")
public class SeminarIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SMnr", nullable = false)
    private Integer id;

    @Column(name = "SMvnr", length = 20)
    private String vnr;

    @Column(name = "SMalternMnr", length = 20)
    private String alternMnr;

    @Column(name = "PJnr", nullable = false)
    private Integer pjnr;

    @Column(name = "SMbezeichnung1", columnDefinition = "char(128)")
    private String bezeichnung1;

    @Column(name = "SMbezeichnung2", columnDefinition = "char(128)")
    private String bezeichnung2;

    @Column(name = "SMtype")
    private Integer type;

    @Column(name = "mbbe_seminartyp")
    private Integer mbbeSeminartyp;

    @Column(name = "infotag")
    private Boolean infotag;

    @Column(name = "SMdatumVon")
    private LocalDate datumVon;

    @Column(name = "SMzeitVon")
    private LocalTime zeitVon;

    @Column(name = "SMdatumBis")
    private LocalDate datumBis;

    @Column(name = "SMzeitBis")
    private LocalTime zeitBis;

    @Column(name = "SMeinheiten")
    private Integer einheiten;

    @Column(name = "SMseminarform", columnDefinition = "TEXT")
    private String seminarform;

    @Column(name = "SMbundesland")
    private Integer bundesland;

    @Column(name = "SMSOstandortid")
    private Integer soStandortId;

    @Column(name = "SMkosten", precision = 9, scale = 2)
    private BigDecimal kosten;

    @Column(name = "SMkostenbem", columnDefinition = "TEXT")
    private String kostenbem;

    @Column(name = "SMabschluss", columnDefinition = "TEXT")
    private String abschluss;

    @Column(name = "SMkurszeiten", columnDefinition = "TEXT")
    private String kurszeiten;

    @Column(name = "SMauftraggeber")
    private Integer auftraggeber;

    @Column(name = "SMauftraggeber_old")
    private Integer auftraggeberOld;

    @Column(name = "SMziel", columnDefinition = "TEXT")
    private String ziel;

    @Column(name = "SMinhalte", columnDefinition = "TEXT")
    private String inhalte;

    @Column(name = "SMvoraussetzung", columnDefinition = "TEXT")
    private String voraussetzung;

    @Column(name = "SMbemerk", columnDefinition = "TEXT")
    private String bemerk;

    @Column(name = "SMvermquote")
    private Integer vermquote;

    @Column(name = "SMnachbetreuungbis")
    private LocalDate nachbetreuungBis;

    @Column(name = "SMnachbesetzungbis")
    private LocalDate nachbesetzungBis;

    @Column(name = "SMpraktikumvon")
    private LocalDate praktikumVon;

    @Column(name = "SMpraktikumbis")
    private LocalDate praktikumBis;

    @Column(name = "SMteilnehmeranzahl")
    private Integer teilnehmeranzahl;

    @Column(name = "SMtnpreis", precision = 9, scale = 2)
    private BigDecimal tnpreis;

    @Column(name = "SMtnsteuersatz", precision = 4, scale = 2)
    private BigDecimal tnsteuersatz;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "SMferienweihnachten")
    private Boolean ferienWeihnachten;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "SMferiensemester")
    private Boolean feriensemester;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "SMferienostern")
    private Boolean ferienOstern;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "SMferiensommer")
    private Boolean ferienSommer;

    @Enumerated(EnumType.STRING)
    @Column(name = "SMtntyp")
    private TNType tntyp;

    @Column(name = "SMmassnahmennr", columnDefinition = "char(20)")
    private String massnahmenNr;

    @Column(name = "SMpublishvon")
    private LocalDate publishVon;

    @Column(name = "SMpublishbis")
    private LocalDate publishBis;

    @Column(name = "SMpublishtxt", columnDefinition = "TEXT")
    private String publishtxt;

    @Column(name = "SMstatus")
    private Integer status;

    @Column(name = "SMdefaulttrainertyp")
    private Integer defaultTrainertyp;

    @Enumerated(EnumType.STRING)
    @Column(name = "SMSourceBelegungen")
    private SourceBelegungen sourceBelegungen = SourceBelegungen.seminar;

    @Column(name = "SMmasterKursKey", length = 256)
    private String masterKursKey;

    @Column(name = "SMbBId", length = 100)
    private String bBId;

    @Column(name = "SMbBaltName", length = 333)
    private String bBaltName;

    @Column(name = "SMbBaktiv")
    private Character bBaktiv;

    @Column(name = "SMbBcreate")
    private Character bBcreate;

    @Column(name = "SMvorlageKey", length = 256)
    private String vorlageKey;

    @Column(name = "SMteamsaltName", length = 333)
    private String teamsaltName;

    @Column(name = "SMteamsaktiv")
    private Character teamsaktiv;

    @Column(name = "SMteamsId", length = 100)
    private String teamsId;

    @Column(name = "SMteamscode", length = 100)
    private String teamscode;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "SMloek")
    private Boolean loek = false;

    @Column(name = "SMaeda")
    private LocalDateTime aeda;

    @Column(name = "SMaeuser", columnDefinition = "char(35)")
    private String aeuser;

    @Column(name = "SMerda")
    private LocalDateTime erda;

    @Column(name = "SMeruser", columnDefinition = "char(35)")
    private String eruser;

    public enum TNType {
        t, h
    }

    public enum SourceBelegungen {
        seminar, workshop
    }
}