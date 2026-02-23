package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Rgs;
import com.ibosng.dbservice.entities.betreuer.Betreuer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.seminar.SeminarAustrittsgrund;
import com.ibosng.dbservice.entities.seminar.SeminarGesamtbeurteilung;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "teilnehmer_2_seminar")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teilnehmer2Seminar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teilnehmer_id")
    private Teilnehmer teilnehmer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seminar_id")
    private Seminar seminar;

    @Column(name = "buchungsstatus")
    private String buchungsstatus;

    @Column(name = "anmerkung")
    private String anmerkung;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "zubuchung")
    private LocalDate zubuchung;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "geplant")
    private LocalDate geplant;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "eintritt")
    private LocalDate eintritt;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "austritt")
    private LocalDate austritt;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "teilnahme_von")
    private LocalDate teilnahmeVon;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "teilnahme_bis")
    private LocalDate teilnahmeBis;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rgs")
    private Rgs rgs;

    @Column(name = "massnahmennummer")
    private String massnahmennummer;

    @Column(name = "veranstaltungsnummer")
    private String veranstaltungsnummer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "betreuer")
    private Betreuer betreuer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TeilnehmerStatus status;

    @Column(name = "import_filename")
    private String importFilename;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;

    @ManyToOne
    @JoinColumn(name = "austrittsgrund", referencedColumnName = "id")
    private SeminarAustrittsgrund austrittsgrund;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "begehren_bis")
    private LocalDate begehrenBis;

    @Column(name = "zusaetzliche_unterstuetzung")
    private String zusaetzlicheUnterstuetzung;

    @Column(name = "lernfortschritt")
    private String lernfortschritt;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "fruehwarnung")
    private LocalDate fruehwarnung;

    @Column(name = "anteil_anwesenheit")
    private Double anteilAnwesenheit;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "gesamtbeurteilung")
    private SeminarGesamtbeurteilung seminarGesamtbeurteilung;

    @Override
    public String toString() {
        return "Teilnehmer2Seminar{" +
                "id=" + id +
                ", teilnehmer=" + teilnehmer != null ? String.valueOf(teilnehmer.getId()) : "" +
                ", seminar=" + seminar != null ? String.valueOf(seminar.getId()) : "" +
                ", buchungsstatus='" + buchungsstatus + '\'' +
                ", anmerkung='" + anmerkung + '\'' +
                ", zubuchung=" + zubuchung +
                ", geplant=" + geplant +
                ", eintritt=" + eintritt +
                ", austritt=" + austritt +
                ", teilnahmeVon=" + teilnahmeVon +
                ", teilnahmeBis=" + teilnahmeBis +
                ", rgs=" + rgs +
                ", massnahmennummer='" + massnahmennummer + '\'' +
                ", veranstaltungsnummer='" + veranstaltungsnummer + '\'' +
                ", betreuer=" + betreuer +
                ", status=" + status +
                ", importFilename='" + importFilename + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                ", austrittsgrund=" + (austrittsgrund != null ? austrittsgrund.getName() : null) +
                ", begehrenBis=" + begehrenBis +
                ", zusaetzlicheUnterstuetzung='" + zusaetzlicheUnterstuetzung + '\'' +
                ", lernfortschritt='" + lernfortschritt + '\'' +
                ", fruehwarnung=" + fruehwarnung +
                ", anteilAnwesenheit=" + anteilAnwesenheit +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Teilnehmer2Seminar that = (Teilnehmer2Seminar) object;
        return Objects.equals(teilnehmer != null ? teilnehmer.getId() : null, that.teilnehmer != null ? that.teilnehmer.getId() : null) &&
                Objects.equals(seminar != null ? seminar.getId() : null, that.seminar != null ? that.seminar.getId() : null) &&
                Objects.equals(buchungsstatus, that.buchungsstatus) &&
                Objects.equals(anmerkung, that.anmerkung) &&
                Objects.equals(zubuchung, that.zubuchung) &&
                Objects.equals(geplant, that.geplant) &&
                Objects.equals(eintritt, that.eintritt) &&
                Objects.equals(austritt, that.austritt) &&
                Objects.equals(rgs, that.rgs) &&
                Objects.equals(massnahmennummer, that.massnahmennummer) &&
                Objects.equals(veranstaltungsnummer, that.veranstaltungsnummer) &&
                Objects.equals(austrittsgrund, that.austrittsgrund) &&
                Objects.equals(begehrenBis, that.begehrenBis) &&
                Objects.equals(zusaetzlicheUnterstuetzung, that.zusaetzlicheUnterstuetzung) &&
                Objects.equals(lernfortschritt, that.lernfortschritt) &&
                Objects.equals(fruehwarnung, that.fruehwarnung) &&
                Objects.equals(anteilAnwesenheit, that.anteilAnwesenheit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teilnehmer != null ? teilnehmer.getId() : null, seminar != null ? seminar.getId() : null, buchungsstatus, anmerkung, zubuchung, geplant, eintritt, austritt, rgs, massnahmennummer, veranstaltungsnummer, austrittsgrund, begehrenBis, zusaetzlicheUnterstuetzung, lernfortschritt, fruehwarnung, anteilAnwesenheit);
    }
}
