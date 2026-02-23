package com.ibosng.dbibosservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "PZMONAT")
@Data
public class PzMonat {
    @EmbeddedId
    private PzMonatId id;

    @Column(name = "PMDZnr")
    private Integer pmdZnr;

    @Column(name = "PMDVtyp", columnDefinition = "ENUM('frei', 'fix')")
    private String pmdVtyp;

    @Column(name = "PMstatus")
    private Integer pmStatus;

    @Column(name = "PMabschlussdatum")
    private LocalDateTime pmAbschlussdatum;

    @Column(name = "PMabschlussIP", length = 16, columnDefinition = "char(16)")
    private String pmAbschlussIP;

    @Column(name = "PMabschlussbenutzer", length = 35, columnDefinition = "char(35)")
    private String pmAbschlussbenutzer;

    @Column(name = "PMsoll", precision = 5, scale = 2)
    private BigDecimal pmSoll;

    @Column(name = "PMist", precision = 5, scale = 2)
    private BigDecimal pmIst;

    @Column(name = "PMurlaub")
    private Integer pmUrlaub;

    @Column(name = "PMzakonsum", precision = 7, scale = 2)
    private BigDecimal pmZakonsum;

    @Column(name = "PMaeda")
    private LocalDateTime pmAeda;

    @Column(name = "PMaeuser", length = 35, columnDefinition = "char(35)")
    private String pmAeuser;

    @Column(name = "PMerda")
    private LocalDateTime pmErda;

    @Column(name = "PMeruser", length = 35, columnDefinition = "char(35)")
    private String pmEruser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PzMonat pzMonat)) return false;
        return Objects.equals(getId(), pzMonat.getId()) && Objects.equals(getPmdZnr(), pzMonat.getPmdZnr()) && Objects.equals(getPmdVtyp(), pzMonat.getPmdVtyp()) && Objects.equals(getPmStatus(), pzMonat.getPmStatus()) && Objects.equals(getPmAbschlussdatum(), pzMonat.getPmAbschlussdatum()) && Objects.equals(getPmAbschlussIP(), pzMonat.getPmAbschlussIP()) && Objects.equals(getPmAbschlussbenutzer(), pzMonat.getPmAbschlussbenutzer()) && Objects.equals(getPmSoll(), pzMonat.getPmSoll()) && Objects.equals(getPmIst(), pzMonat.getPmIst()) && Objects.equals(getPmUrlaub(), pzMonat.getPmUrlaub()) && Objects.equals(getPmZakonsum(), pzMonat.getPmZakonsum()) && Objects.equals(getPmAeda(), pzMonat.getPmAeda()) && Objects.equals(getPmAeuser(), pzMonat.getPmAeuser()) && Objects.equals(getPmErda(), pzMonat.getPmErda()) && Objects.equals(getPmEruser(), pzMonat.getPmEruser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPmdZnr(), getPmdVtyp(), getPmStatus(), getPmAbschlussdatum(), getPmAbschlussIP(), getPmAbschlussbenutzer(), getPmSoll(), getPmIst(), getPmUrlaub(), getPmZakonsum(), getPmAeda(), getPmAeuser(), getPmErda(), getPmEruser());
    }
}
