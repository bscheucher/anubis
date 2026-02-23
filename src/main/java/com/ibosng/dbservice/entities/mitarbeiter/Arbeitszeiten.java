package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "arbeitszeiten")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Arbeitszeiten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "arbeitszeiten_info_id", referencedColumnName = "id")
    private ArbeitszeitenInfo arbeitszeitenInfo;

    @Column(name = "montag_von")
    private LocalTime montagVon;

    @Column(name = "montag_bis")
    private LocalTime montagBis;

    @Column(name = "montag_netto")
    private Double montagNetto;

    @Column(name = "dienstag_von")
    private LocalTime dienstagVon;

    @Column(name = "dienstag_bis")
    private LocalTime dienstagBis;

    @Column(name = "dienstag_netto")
    private Double dienstagNetto;

    @Column(name = "mittwoch_von")
    private LocalTime mittwochVon;

    @Column(name = "mittwoch_bis")
    private LocalTime mittwochBis;

    @Column(name = "mittwoch_netto")
    private Double mittwochNetto;

    @Column(name = "donnerstag_von")
    private LocalTime donnerstagVon;

    @Column(name = "donnerstag_bis")
    private LocalTime donnerstagBis;

    @Column(name = "donnerstag_netto")
    private Double donnerstagNetto;

    @Column(name = "freitag_von")
    private LocalTime freitagVon;

    @Column(name = "freitag_bis")
    private LocalTime freitagBis;

    @Column(name = "freitag_netto")
    private Double freitagNetto;

    @Column(name = "samstag_von")
    private LocalTime samstagVon;

    @Column(name = "samstag_bis")
    private LocalTime samstagBis;

    @Column(name = "samstag_netto")
    private Double samstagNetto;

    @Column(name = "sonntag_von")
    private LocalTime sonntagVon;

    @Column(name = "sonntag_bis")
    private LocalTime sonntagBis;

    @Column(name = "sonntag_netto")
    private Double sonntagNetto;

    @Column(name = "is_kernzeit")
    private Boolean kernzeit;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Arbeitszeiten{" +
                "id=" + id +
                ", arbeitszeitenInfo=" + arbeitszeitenInfo +
                ", montagVon=" + montagVon +
                ", montagBis=" + montagBis +
                ", montagNetto=" + montagNetto +
                ", dienstagVon=" + dienstagVon +
                ", dienstagBis=" + dienstagBis +
                ", dienstagNetto=" + dienstagNetto +
                ", mittwochVon=" + mittwochVon +
                ", mittwochBis=" + mittwochBis +
                ", mittwochNetto=" + mittwochNetto +
                ", donnerstagVon=" + donnerstagVon +
                ", donnerstagBis=" + donnerstagBis +
                ", donnerstagNetto=" + donnerstagNetto +
                ", freitagVon=" + freitagVon +
                ", freitagBis=" + freitagBis +
                ", freitagNetto=" + freitagNetto +
                ", samstagVon=" + samstagVon +
                ", samstagBis=" + samstagBis +
                ", samstagNetto=" + samstagNetto +
                ", sonntagVon=" + sonntagVon +
                ", sonntagBis=" + sonntagBis +
                ", sonntagNetto=" + sonntagNetto +
                ", kernzeit=" + kernzeit +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Arbeitszeiten that = (Arbeitszeiten) object;
        return  Objects.equals(arbeitszeitenInfo != null ? arbeitszeitenInfo.getId() : null, that.arbeitszeitenInfo != null ? that.arbeitszeitenInfo.getId() : null) &&
                Objects.equals(montagVon, that.montagVon) &&
                Objects.equals(montagBis, that.montagBis) &&
                Objects.equals(montagNetto, that.montagNetto) &&
                Objects.equals(dienstagVon, that.dienstagVon) &&
                Objects.equals(dienstagBis, that.dienstagBis) &&
                Objects.equals(dienstagNetto, that.dienstagNetto) &&
                Objects.equals(mittwochVon, that.mittwochVon) &&
                Objects.equals(mittwochBis, that.mittwochBis) &&
                Objects.equals(mittwochNetto, that.mittwochNetto) &&
                Objects.equals(donnerstagVon, that.donnerstagVon) &&
                Objects.equals(donnerstagBis, that.donnerstagBis) &&
                Objects.equals(donnerstagNetto, that.donnerstagNetto) &&
                Objects.equals(freitagVon, that.freitagVon) &&
                Objects.equals(freitagBis, that.freitagBis) &&
                Objects.equals(freitagNetto, that.freitagNetto) &&
                Objects.equals(samstagVon, that.samstagVon) &&
                Objects.equals(samstagBis, that.samstagBis) &&
                Objects.equals(samstagNetto, that.samstagNetto) &&
                Objects.equals(sonntagVon, that.sonntagVon) &&
                Objects.equals(sonntagBis, that.sonntagBis) &&
                Objects.equals(sonntagNetto, that.sonntagNetto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(arbeitszeitenInfo != null ? arbeitszeitenInfo.getId() : null,
                montagVon,
                montagBis,
                montagNetto,
                dienstagVon,
                dienstagBis,
                dienstagNetto,
                mittwochVon,
                mittwochBis,
                mittwochNetto,
                donnerstagVon,
                donnerstagBis,
                donnerstagNetto,
                freitagVon,
                freitagBis,
                freitagNetto,
                samstagVon,
                samstagBis,
                samstagNetto,
                sonntagVon,
                sonntagBis,
                sonntagNetto);
    }
}
