package com.ibosng.dbibosservice.entities.mitarbeiter;

import com.ibosng.dbibosservice.converters.BooleanStatusConverter;
import com.ibosng.dbibosservice.enums.BooleanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DIENSTVERTRAG")
@Data
public class DienstvertragIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DVnr", nullable = false, updatable = false)
    private Integer dvNr;

    @Column(name = "ADadnr", nullable = false, columnDefinition = "INT(6) UNSIGNED")
    private Integer adAdnr;

    @Column(name = "PBid", columnDefinition = "INT(6) UNSIGNED")
    private Integer pbId;

    @Column(name = "ARBEITSVERTRAG_id", columnDefinition = "INT(6) UNSIGNED")
    private Integer arbeitsvertragId;

    @Column(name = "DVkstgr", columnDefinition = "INT(3)")
    private Integer dvKstgr;

    @Enumerated(EnumType.STRING)
    @Column(name = "DVtyp")
    private Vertragstyp dvTyp;

    @Column(name = "DVfirma", length = 100, columnDefinition = "varchar(100)")
    private String dvFirma;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "DVgewerbeschein")
    private BooleanStatus dvGewerbeschein;

    @Column(name = "DVgewerbeart", length = 40, columnDefinition = "char(40)")
    private String dvGewerbeart;

    @Column(name = "DVbehoerde", length = 40, columnDefinition = "char(40)")
    private String dvBehoerde;

    @Column(name = "DVgszahl", length = 20, columnDefinition = "char(20)")
    private String dvGszahl;

    @Column(name = "DVgsgueltigab")
    private LocalDate dvGsgueltigab;

    @Column(name = "DVlohngruppe", columnDefinition = "INT(6) UNSIGNED")
    private Integer dvLohngruppe;

    @Column(name = "DVlohnstufe", columnDefinition = "INT(6) UNSIGNED")
    private Integer dvLohnstufe;

    @Column(name = "DVdatumeintritt")
    private LocalDate dvDatumeintritt;

    @Column(name = "DVdatumaustritt")
    private LocalDate dvDatumaustritt;

    @Column(name = "DVanmeldungdatum")
    private LocalDate dvAnmeldungdatum;

    @Column(name = "DVanmeldungsachb", length = 35, columnDefinition = "varchar(35)")
    private String dvAnmeldungsachb;

    @Column(name = "DVabmeldungdatum")
    private LocalDate dvAbmeldungdatum;

    @Column(name = "DVabmeldungsachb", length = 35, columnDefinition = "varchar(35)")
    private String dvAbmeldungsachb;

    @Column(name = "DVmitversichert", columnDefinition = "TEXT")
    private String dvMitversichert;

    @Column(name = "DVvordienst", length = 40, columnDefinition = "char(40)")
    private String dvVordienst;

    @Column(name = "DVanstellungsart", columnDefinition = "INT(6)")
    private Integer dvAnstellungsart;

    @Column(name = "DVfunktion", columnDefinition = "INT(6)")
    private Integer dvFunktion;

    @Column(name = "DVdienstgeber", columnDefinition = "INT(6) UNSIGNED")
    private Integer dvDienstgeber;

    @Column(name = "DVfunktionstr", length = 128, columnDefinition = "char(128)")
    private String dvFunktionstr;

    @Column(name = "DVstatus", columnDefinition = "INT(1)")
    private Integer dvStatus;

    @Column(name = "DVkarenz", columnDefinition = "TINYINT(1)")
    private Boolean dvKarenz;

    @Column(name = "DVkurzarbeit", columnDefinition = "TINYINT(1)")
    private Boolean dvKurzarbeit;

    @Column(name = "DVgesperrt", columnDefinition = "TINYINT(1)")
    private Boolean dvGesperrt;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "DVloek")
    private BooleanStatus dvLoek;

    @Column(name = "DVaeda", columnDefinition = "DATETIME")
    private LocalDateTime dvAeda;

    @Column(name = "DVaeuser", length = 35, columnDefinition = "char(351)")
    private String dvAeuser;

    @Column(name = "DVerda", columnDefinition = "DATETIME")
    private LocalDateTime dvErda;

    @Column(name = "DVeruser", length = 35, columnDefinition = "char(35)")
    private String dvEruser;

    public enum Vertragstyp {
        frei,
        fix
    }

}

