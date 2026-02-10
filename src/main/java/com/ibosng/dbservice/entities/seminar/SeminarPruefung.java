package com.ibosng.dbservice.entities.seminar;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "seminar_pruefung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarPruefung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "bezeichnung", referencedColumnName = "id")
    private SeminarPruefungBezeichnung bezeichnung;

    @ManyToOne
    @JoinColumn(name = "pruefung_art", referencedColumnName = "id")
    private SeminarPruefungArt pruefungArt;

    @ManyToOne
    @JoinColumn(name = "gegenstand", referencedColumnName = "id")
    private SeminarPruefungGegenstand gegenstand;

    @ManyToOne
    @JoinColumn(name = "niveau", referencedColumnName = "id")
    private SeminarPruefungNiveau niveau;

    @ManyToOne
    @JoinColumn(name = "institut", referencedColumnName = "id")
    private SeminarPruefungInstitut institut;

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
    @JoinColumn(name = "teilnehmer_2_seminar")
    private Teilnehmer2Seminar teilnehmer2Seminar;

    @Column(name = "antritt")
    private Boolean antritt;

    @ManyToOne
    @JoinColumn(name = "begruendung")
    private SeminarPruefungBegruendung begruendung;

    @ManyToOne
    @JoinColumn(name = "ergebnis_type")
    private SeminarPruefungErgebnisType ergebnisType;

    @Column(name = "ergebnis_in_prozent")
    private Integer ergebnisInProzent;

    @Column(name = "pruefung_am")
    private LocalDate pruefungAm;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeminarPruefung that = (SeminarPruefung) o;
        return Objects.equals(bezeichnung != null ? bezeichnung.getId() : null, that.bezeichnung != null ? that.bezeichnung.getId() : null) &&
                Objects.equals(pruefungArt != null ? pruefungArt.getId() : null, that.pruefungArt != null ? that.pruefungArt.getId() : null) &&
                Objects.equals(gegenstand != null ? gegenstand.getId() : null, that.gegenstand != null ? that.gegenstand.getId() : null) &&
                Objects.equals(niveau != null ? niveau.getId() : null, that.niveau != null ? that.niveau.getId() : null) &&
                Objects.equals(teilnehmer2Seminar != null ? teilnehmer2Seminar.getId() : null, that.teilnehmer2Seminar != null ? that.teilnehmer2Seminar.getId() : null) &&
                Objects.equals(institut != null ? institut.getId() : null, that.institut != null ? that.institut.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                bezeichnung != null ? bezeichnung.getId() : null,
                pruefungArt != null ? pruefungArt.getId() : null,
                gegenstand != null ? gegenstand.getId() : null,
                niveau != null ? niveau.getId() : null,
                teilnehmer2Seminar != null ? teilnehmer2Seminar.getId() : null,
                institut != null ? institut.getId() : null
        );
    }

    @Override
    public String toString() {
        return "SeminarPruefung{" +
                "bezeichnung=" + (bezeichnung != null ? bezeichnung.getName() : "null") +
                ", pruefungArt=" + (pruefungArt != null ? pruefungArt.getName() : "null") +
                ", gegenstand=" + (gegenstand != null ? gegenstand.getName() : "null") +
                ", niveau=" + (niveau != null ? niveau.getName() : "null") +
                ", teilnehmer2Seminar=" + (teilnehmer2Seminar != null ? teilnehmer2Seminar.getId() : "null") +
                ", institut=" + (institut != null ? institut.getName() : "null") +
                '}';
    }
}
