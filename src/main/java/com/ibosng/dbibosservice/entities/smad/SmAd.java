package com.ibosng.dbibosservice.entities.smad;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "SM_AD")
public class SmAd {

    @EmbeddedId
    private SmAdId smAdId;

    @Column(name = "ADRESSE_ADadnr", nullable = false)
    private Integer adresseAdadnr;

    @Column(name = "DIENSTVERTRAG_DVnr", nullable = false)
    private Integer dienstvertragDvnr = 0;

    @Column(name = "SMADdatumvon")
    private LocalDate smaddatumvon;

    @Column(name = "SMADdatumbis")
    private LocalDate smaddatumbis;

    @Column(name = "SMADersetzt")
    private Integer smadersetzt;

    @Column(name = "SMADbezeichnung", columnDefinition = "char(80)")
    private String smadbezeichnung;

    @Column(name = "SMADinhalte", columnDefinition = "TEXT")
    private String smadinhalte;

    @Column(name = "SMADtaetigkeit", columnDefinition = "char(40)")
    private String smadtaetigkeit;

    @Column(name = "SMADtyp")
    private Integer smadtyp;

    @Column(name = "SMADfunktion")
    private Integer smadfunktion;

    @Column(name = "SMADbewertung")
    private Integer smadbewertung;

    @Column(name = "SMADeinheiten")
    private Integer smadeinheiten;

    @Column(name = "SMADeinheitenbem", columnDefinition = "char(40)")
    private String smadeinheitenbem;

    @Column(name = "SMADstundensatz", precision = 9, scale = 2)
    private BigDecimal smadstundensatz;

    @Column(name = "SMADref")
    private Integer smadref;

    @Column(name = "SMADbemerkung", columnDefinition = "TEXT")
    private String smadbemerkung;

    @Column(name = "SMADanfdatum")
    private LocalDateTime smadanfdatum;

    @Column(name = "SMADanfuser", columnDefinition = "char(15)")
    private String smadanfuser;

    @Column(name = "SMADzugdatum")
    private LocalDateTime smadzugdatum;

    @Column(name = "SMADzuguser", columnDefinition = "char(15)")
    private String smadzuguser;

    @Column(name = "SMADstatus")
    private Integer smadstatus;

    @Column(name = "SMADseminarbuch")
    private Boolean smadseminarbuch = false;

    @Column(name = "SMADtrainertyp")
    private Integer smadtrainertyp;

    @Column(name = "SM_ADpktErf", length = 2)
    private String smAdpktErf = "-";

    @Column(name = "SM_ADpktqual", length = 2)
    private String smAdpktqual = "-";

    @Column(name = "SMADbBcreate")
    private Character smadbBcreate;

    @Column(name = "SMADTeamscreate")
    private Character smadTeamscreate;

    @Column(name = "SMADaeda")
    private LocalDateTime smadaeda;

    @Column(name = "SMADaeuser", columnDefinition = "char(35)")
    private String smadaeuser;

    @Column(name = "SMADerda")
    private LocalDateTime smaderda;

    @Column(name = "SMADeruser", columnDefinition = "char(35)")
    private String smaderuser;

    @Enumerated(EnumType.STRING)
    @Column(name = "SMADcckontakt", nullable = false)
    private ContactType smadcckontakt = ContactType.n;

    // Enum for contact type
    public enum ContactType {
        e, y, n
    }

    // Class for composite primary key

}
