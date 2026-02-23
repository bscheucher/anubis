package com.ibosng.dbibosservice.entities.mitarbeiter;

import com.ibosng.dbibosservice.converters.BooleanStatusConverter;
import com.ibosng.dbibosservice.enums.BooleanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "DVZUSATZ")
@Data
public class DvZusatzIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DZnr", nullable = false, updatable = false)
    private Integer dzNr;

    @Column(name = "ADadnr", nullable = false, columnDefinition = "INT(6) UNSIGNED")
    private Integer adAdnr;

    @Column(name = "DVnr", nullable = false, columnDefinition = "INT(6) UNSIGNED")
    private Integer dvNr;

    @Column(name = "DZpersnr", length = 10, columnDefinition = "char(10)")
    private String dzPersnr;

    @Column(name = "DZdatumab", columnDefinition = "DATE")
    private LocalDate dzDatumAb;

    @Column(name = "DZlohn", precision = 9, scale = 2, columnDefinition = "DECIMAL(9, 2)")
    private BigDecimal dzLohn;

    @Column(name = "DZpraemie", precision = 9, scale = 2, columnDefinition = "DECIMAL(9, 2)")
    private BigDecimal dzPraemie;

    @Column(name = "DZhonorar", length = 40, columnDefinition = "char(40)")
    private String dzHonorar;

    @Column(name = "DZfahrtkosten", precision = 9, scale = 2, columnDefinition = "DECIMAL(9, 2)")
    private BigDecimal dzFahrtkosten;

    @Column(name = "DZfahrkostenvereinb", length = 128, columnDefinition = "char(128)")
    private String dzFahrkostenvereinb;

    @Column(name = "DZbemerkungen", columnDefinition = "TEXT")
    private String dzBemerkungen;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "DZsteuerbefreit")
    private BooleanStatus dzSteuerbefreit;

    @Column(name = "DZazmo", precision = 4, scale = 2, columnDefinition = "DECIMAL(4, 2)")
    private BigDecimal dzAzmo;

    @Column(name = "DZazdi", precision = 4, scale = 2, columnDefinition = "DECIMAL(4, 2)")
    private BigDecimal dzAzdi;

    @Column(name = "DZazmi", precision = 4, scale = 2, columnDefinition = "DECIMAL(4, 2)")
    private BigDecimal dzAzmi;

    @Column(name = "DZazdo", precision = 4, scale = 2, columnDefinition = "DECIMAL(4, 2)")
    private BigDecimal dzAzdo;

    @Column(name = "DZazfr", precision = 4, scale = 2, columnDefinition = "DECIMAL(4, 2)")
    private BigDecimal dzAzfr;

    @Column(name = "DZazsa", precision = 4, scale = 2, columnDefinition = "DECIMAL(4, 2)")
    private BigDecimal dzAzsa;

    @Column(name = "DZazso", precision = 4, scale = 2, columnDefinition = "DECIMAL(4, 2)")
    private BigDecimal dzAzso;

    @Column(name = "DZwochenstd", columnDefinition = "INT(3) DEFAULT 0")
    private Integer dzWochenstd;

    @Column(name = "DZurlaub", precision = 3, scale = 2, columnDefinition = "DECIMAL(3, 2)")
    private BigDecimal dzUrlaub;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "DZbebucht")
    private BooleanStatus dzBebucht;

    @Column(name = "DZmeldepflicht", columnDefinition = "INT(1)")
    private Integer dzMeldepflicht;

    @Column(name = "DZmeldungdatum", columnDefinition = "DATE")
    private LocalDate dzMeldungDatum;

    @Column(name = "DZmeldungsachb", length = 35, columnDefinition = "varchar(35)")
    private String dzMeldungsAchb;

    @Column(name = "DZmittagspause_ignorieren", columnDefinition = "TINYINT(1)")
    private Boolean dzMittagspauseIgnorieren;

    @Column(name = "DZstatus", columnDefinition = "INT(1)")
    private Integer dzStatus;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "DZloek")
    private BooleanStatus dzLoek;

    @Column(name = "DZaeda", columnDefinition = "DATETIME")
    private LocalDateTime dzAeda;

    @Column(name = "DZaeuser", length = 35, columnDefinition = "char(35)")
    private String dzAeuser;

    @Column(name = "DZerda", columnDefinition = "DATETIME")
    private LocalDateTime dzErda;

    @Column(name = "DZeruser", length = 35, columnDefinition = "char(35)")
    private String dzEruser;

}
