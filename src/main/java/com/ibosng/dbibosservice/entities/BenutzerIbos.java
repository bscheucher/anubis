package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "BENUTZER")
public class BenutzerIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BNid", nullable = false)
    private Integer bnid;

    @Column(name = "BNadID", length = 45)
    private String bnadId;

    @Column(name = "BNadSAMAN", length = 45)
    private String bnadSaman;

    @Column(name = "BNupn", length = 45)
    private String bnupn;

    @Column(name = "BNuserid", length = 35)
    private String bnuserid;

    @Column(name = "BNpwd", length = 255)
    private String bnpwd;

    @Column(name = "BNpwd_mustchange")
    private Boolean bnpwdMustChange = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "BNtyp", columnDefinition = "ENUM('IBOS', 'TC')")
    private BnTyp bntyp = BnTyp.IBOS;

    @Column(name = "BNaktivbis")
    private LocalDate bnaktivbis;

    @Column(name = "BNbemerk", columnDefinition = "TEXT")
    private String bnbemerk;

    @Column(name = "BNlistlg")
    private Integer bnlistlg = 3;

    @Enumerated(EnumType.STRING)
    @Column(name = "BNreiteransicht", columnDefinition = "ENUM('einzeilig', 'mehrzeilig')")
    private BnReiteransicht bnreiteransicht = BnReiteransicht.mehrzeilig;

    @Column(name = "BNlastlogin")
    private LocalDateTime bnlastlogin;

    @Column(name = "BNlastAktiv")
    private LocalDateTime bnlastAktiv;

    @Column(name = "BNadnr")
    private Integer bnadnr;

    @Column(name = "BNmailsent")
    private LocalDateTime bnmailsent;

    @Column(name = "BNfailmessage", columnDefinition = "TEXT")
    private String bnfailmessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "BNloek", columnDefinition = "ENUM('n', 'y')")
    private BnLoek bnloek = BnLoek.n;

    @Column(name = "BNaeda")
    private LocalDateTime bnaeda;

    @Column(name = "BNaeuser", columnDefinition = "char(35)")
    private String bnaeuser;

    @Column(name = "BNerda", nullable = false)
    private LocalDateTime bnerda;

    @Column(name = "BNeruser", columnDefinition = "char(35)", nullable = false)
    private String bneruser;

    // Enums for the 'enum' columns
    public enum BnTyp {
        IBOS, TC
    }

    public enum BnReiteransicht {
        einzeilig, mehrzeilig
    }

    public enum BnLoek {
        n, y
    }

    // NoArgsConstructor, AllArgsConstructor, Getters, Setters, Equals, HashCode, ToString
}

