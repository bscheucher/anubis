package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "tn_abschluss")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TnAbschluss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teilnehmer")
    private Teilnehmer teilnehmer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "abschluss_kategorie")
    private TnAbschlussKategorie abschlussKategorie;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "abschluss_am")
    private LocalDate abschlussAm;

    @Column(name = "notiz")
    private String notiz;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TnAbschluss that = (TnAbschluss) o;
        return Objects.equals(teilnehmer, that.teilnehmer) &&
                Objects.equals(abschlussKategorie, that.abschlussKategorie) &&
                Objects.equals(abschlussAm, that.abschlussAm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teilnehmer, abschlussKategorie, abschlussAm);
    }

    @Override
    public String toString() {
        return "TnAbschluss{" +
                "id=" + id +
                ", teilnehmer=" + (teilnehmer != null ? teilnehmer.getId() : null) +
                ", abschlussKategorie=" + (abschlussKategorie != null ? abschlussKategorie.getId() : null) +
                ", abschlussAm=" + abschlussAm +
                ", notiz='" + notiz + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
}
