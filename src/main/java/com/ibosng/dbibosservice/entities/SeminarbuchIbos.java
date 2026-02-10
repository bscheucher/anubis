package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "SEMINARBUCH")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarbuchIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SBnr")
    private Integer sbnr;

    @Column(name = "ADadnr", nullable = false)
    private Integer adadnr;

    @Column(name = "SMnr", nullable = false)
    private Integer smnr;

    @Column(name = "SBdatum")
    private LocalDate sbdatum;

    @Column(name = "SBvon")
    private LocalTime sbvon;

    @Column(name = "SBbis")
    private LocalTime sbbis;

    @Column(name = "SBeinheiten", precision = 4, scale = 2)
    private BigDecimal sbeinheiten;

    @Column(name = "SBlehrstoff")
    private String sblehrstoff;

    @Column(name = "SBbemerkung")
    private String sbbemerkung;

    @Column(name = "SBstatus")
    private Integer sbstatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "SBloek")
    private YesNo sbloek;

    @Column(name = "SBaeda")
    private LocalDateTime sbaeda;

    @Column(name = "SBaeuser", columnDefinition = "char(35)")
    private String sbaeuser;

    @Column(name = "SBerda")
    private LocalDateTime sberda;

    @Column(name = "SBeruser", columnDefinition = "char(35)")
    private String sberuser;

    // Enum for 'SBloek' column
    public enum YesNo {
        n, y
    }
}
