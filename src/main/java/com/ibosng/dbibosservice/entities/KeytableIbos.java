package com.ibosng.dbibosservice.entities;

import com.ibosng.dbibosservice.converters.BooleanStatusConverter;
import com.ibosng.dbibosservice.enums.BooleanStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "KEYTABLE")
@Data
public class KeytableIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "KYnr", nullable = false, updatable = false, columnDefinition = "INT(6) UNSIGNED")
    private Integer kyNr;

    @Column(name = "KYname", length = 20, columnDefinition = "VARCHAR(20)")
    private String kyName;

    @Column(name = "KYindex", columnDefinition = "INT(3) UNSIGNED")
    private Integer kyIndex;

    @Column(name = "KYvaluet1", length = 100, columnDefinition = "VARCHAR(100)")
    private String kyValueT1;

    @Column(name = "KYvaluet2", length = 100, columnDefinition = "VARCHAR(100)")
    private String kyValueT2;

    @Column(name = "KYvaluet3", length = 100, columnDefinition = "VARCHAR(100)")
    private String kyValueT3;

    @Column(name = "KYvaluet4", length = 100, columnDefinition = "VARCHAR(100)")
    private String kyValueT4;

    @Column(name = "KYvaluet5", length = 100, columnDefinition = "VARCHAR(100)")
    private String kyValueT5;

    @Column(name = "KYvaluem1", columnDefinition = "TEXT")
    private String kyValueM1;

    @Column(name = "KYvaluenum1", columnDefinition = "INT UNSIGNED")
    private Integer kyValueNum1;

    @Column(name = "KYvaluenum2", columnDefinition = "INT UNSIGNED")
    private Integer kyValueNum2;

    @Column(name = "KYvaluedate1", columnDefinition = "INT UNSIGNED")
    private Integer kyValueDate1;

    @Column(name = "KYvaluedate2", columnDefinition = "INT UNSIGNED")
    private Integer kyValueDate2;

    @Column(name = "KYvisible", columnDefinition = "TINYINT(1)")
    private Boolean kyVisible;

    @Column(name = "KYbemerkung", columnDefinition = "INT UNSIGNED")
    private Integer kyBemerkung;

    @Column(name = "KYcodeart", length = 4, columnDefinition = "CHAR(4) DEFAULT 'user' NOT NULL")
    private String kyCodeArt;

    @Convert(converter = BooleanStatusConverter.class)
    @Column(name = "KYloek")
    private BooleanStatus kyLoek;

    @Column(name = "KYaeda", columnDefinition = "DATETIME")
    private LocalDateTime kyAeda;

    @Column(name = "KYaeuser", length = 35, columnDefinition = "CHAR(35)")
    private String kyAeuser;

    @Column(name = "KYerda", columnDefinition = "DATETIME")
    private LocalDateTime kyErda;

    @Column(name = "KYeruser", length = 35, columnDefinition = "CHAR(35)")
    private String kyEruser;


}
