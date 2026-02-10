package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "FKOSTENSTELLE")
@Data
public class FKostenstelleIbos {

    @Embeddable
    @Data
    public static class FKostenstelleId implements Serializable {

        @Column(name = "KSTKSTGR", nullable = false)
        private Integer kstKstGr;

        @Column(name = "KSTKSTNR", nullable = false)
        private Integer kstKstNr;

        @Column(name = "KSTKSTSUB", nullable = false)
        private Integer kstKstSub;

        // Overriding equals() and hashCode() for proper composite key handling
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FKostenstelleId that = (FKostenstelleId) o;
            return Objects.equals(kstKstGr, that.kstKstGr) &&
                    Objects.equals(kstKstNr, that.kstKstNr) &&
                    Objects.equals(kstKstSub, that.kstKstSub);
        }

        @Override
        public int hashCode() {
            return Objects.hash(kstKstGr, kstKstNr, kstKstSub);
        }
    }

    @EmbeddedId
    private FKostenstelleId id;

    @Column(name = "KSTbezeichnung", length = 30, columnDefinition = "VARCHAR(30)")
    private String kstBezeichnung;

    @Column(name = "KSTkurz", length = 20, columnDefinition = "VARCHAR(20)")
    private String kstKurz;

    @Column(name = "KSTverantwortlich", length = 50, columnDefinition = "VARCHAR(50)")
    private String kstVerantwortlich;

    @Column(name = "KSTbemerkung", columnDefinition = "TEXT")
    private String kstBemerkung;

    @Column(name = "KSTanschrift", columnDefinition = "TEXT")
    private String kstAnschrift;

    @Column(name = "KSTadrbez1", length = 50, columnDefinition = "VARCHAR(50)")
    private String kstAdrbez1;

    @Column(name = "KSTadrbez2", length = 50, columnDefinition = "VARCHAR(50)")
    private String kstAdrbez2;

    @Column(name = "KSTadrbez3", length = 50, columnDefinition = "VARCHAR(50)")
    private String kstAdrbez3;

    @Column(name = "KSTadrplz", length = 6, columnDefinition = "VARCHAR(6)")
    private String kstAdrplz;

    @Column(name = "KSTadrort", length = 50, columnDefinition = "VARCHAR(50)")
    private String kstAdrort;

    @Column(name = "KSTadrstrasse", length = 50, columnDefinition = "VARCHAR(50)")
    private String kstAdrstrasse;

    @Column(name = "KSTadrtel", length = 20, columnDefinition = "VARCHAR(20)")
    private String kstAdrtel;

    @Column(name = "KSTadrfax", length = 20, columnDefinition = "VARCHAR(20)")
    private String kstAdrfax;

    @Column(name = "KSTadremail", length = 80, columnDefinition = "VARCHAR(80)")
    private String kstAdremail;

    @Column(name = "KSTbereichsleiter", columnDefinition = "INT UNSIGNED")
    private Integer kstBereichsleiter;

    @Enumerated(EnumType.STRING)
    @Column(name = "KSTbuchsperre")
    private Buchsperre kstBuchsperre;

    @Column(name = "KSTstatus", columnDefinition = "INT(1) UNSIGNED")
    private Integer kstStatus;

    @Column(name = "KSTcognoscode", length = 5, columnDefinition = "VARCHAR(5)")
    private String kstCognoscode;

    @Enumerated(EnumType.STRING)
    @Column(name = "KSTloek", columnDefinition = "ENUM('n', 'y') DEFAULT 'n'")
    private Loek kstLoek;

    @Column(name = "KSTerda", columnDefinition = "DATETIME")
    private LocalDateTime kstErda;

    @Column(name = "KSTeruser", length = 35, columnDefinition = "CHAR(35)")
    private String kstEruser;

    @Column(name = "KSTaeda", columnDefinition = "DATETIME")
    private LocalDateTime kstAeda;

    @Column(name = "KSTaeuser", length = 35, columnDefinition = "CHAR(35)")
    private String kstAeuser;

    // Enums for the ENUM columns
    public enum Buchsperre {
        n,
        y
    }

    public enum Loek {
        n,
        y
    }
}
