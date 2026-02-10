package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.Muttersprache;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "sprachkenntnis")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sprachkenntnis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sprache")
    private Muttersprache sprache;

    @ManyToOne
    @JoinColumn(name = "sprache_niveau")
    private SprachkenntnisNiveau spracheNiveau;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "changed_by")
    private String changedBy;

    @ManyToOne
    @JoinColumn(name = "teilnehmer")
    private Teilnehmer teilnehmer;

    @ManyToOne
    @JoinColumn(name = "bewertung_coach", referencedColumnName = "id")
    private SprachkenntnisNiveau bewertungCoach;

    @Column(name = "bewertung_datum")
    private LocalDate bewertungDatum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprachkenntnis that = (Sprachkenntnis) o;
        return Objects.equals(sprache != null ? sprache.getId() : null, that.spracheNiveau != null ? that.spracheNiveau.getId() : null) &&
                Objects.equals(teilnehmer != null ? teilnehmer.getId() : null, that.bewertungCoach != null ? that.bewertungCoach.getId() : null) &&
                Objects.equals(bewertungDatum, that.bewertungDatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sprache != null ? sprache.getId() : null,
                spracheNiveau != null ? spracheNiveau.getId() : null,
                teilnehmer != null ? teilnehmer.getId() : null,
                bewertungCoach != null ? bewertungCoach.getId() : null,
                bewertungDatum);
    }

    @Override
    public String toString() {
        return "Sprachkenntnis{" +
                "sprache=" + (sprache != null ? sprache.getName() : "null") +
                ", spracheNiveau=" + (spracheNiveau != null ? spracheNiveau.getName() : "null") +
                ", teilnehmer=" + (teilnehmer != null ? teilnehmer.getId() : "null") +
                ", bewertungCoach=" + (bewertungCoach != null ? bewertungCoach.getName() : "null") +
                ", bewertungDatum=" + bewertungDatum +
                '}';
    }
}
