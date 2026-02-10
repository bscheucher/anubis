package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Constants.DATE_PATTERN;

@Entity
@Table(name = "projekt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projekt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "projekt_nummer")
    Integer projektNummer;

    @Column(name = "auftrag_nummer")
    Integer auftragNummer;

    @Column(name = "bezeichnung")
    String bezeichnung;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    @Column(name = "end_date")
    LocalDate endDate;

    @Column(name = "kostenstelle_gruppe")
    Integer kostenstelleGruppe;

    @Column(name = "kostenstelle")
    Integer kostenstelle;

    @Column(name = "kostentraeger")
    Integer kostentraeger;

    @Column(name = "kostentraeger_display_name")
    String kostentraegerDisplayName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projekt_type")
    private ProjektType projektType;

    @OneToMany(mappedBy = "projekt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Projekt2Manager> trainerSeminars = new ArrayList<>();

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    LocalDateTime createdOn = LocalDateTime.now();

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    LocalDateTime changedOn = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "created_by")
    String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    String changedBy;

    @ManyToOne
    @JoinColumn(name = "ausschreibung_id")
    private Ausschreibung ausschreibung;

    @Transient
    private boolean shouldSync = true;

    @Override
    public String toString() {
        return "Projekt{" +
                "id=" + id +
                ", projektNummer=" + projektNummer +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", kostenstelleGruppe=" + kostenstelleGruppe +
                ", kostenstelle=" + kostenstelle +
                ", kostentraeger=" + kostentraeger +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projekt projekt = (Projekt) o;
        return Objects.equals(id, projekt.id) &&
                Objects.equals(projektNummer, projekt.projektNummer) &&
                Objects.equals(auftragNummer, projekt.auftragNummer) &&
                Objects.equals(bezeichnung, projekt.bezeichnung) &&
                Objects.equals(startDate, projekt.startDate) &&
                Objects.equals(endDate, projekt.endDate) &&
                Objects.equals(kostenstelleGruppe, projekt.kostenstelleGruppe) &&
                Objects.equals(kostenstelle, projekt.kostenstelle) &&
                Objects.equals(kostentraeger, projekt.kostentraeger) &&
                Objects.equals(kostentraegerDisplayName, projekt.kostentraegerDisplayName) &&
                Objects.equals(projektType, projekt.projektType) &&
                Objects.equals(ausschreibung, projekt.ausschreibung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projektNummer, auftragNummer, bezeichnung, startDate, endDate, kostenstelleGruppe, kostenstelle, kostentraeger, kostentraegerDisplayName, projektType, ausschreibung);
    }
}
