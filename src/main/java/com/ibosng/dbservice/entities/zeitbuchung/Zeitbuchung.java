package com.ibosng.dbservice.entities.zeitbuchung;

import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.entities.seminar.Seminar;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "zeitbuchung")
@Data
public class Zeitbuchung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "von")
    private LocalTime von;

    @Column(name = "bis")
    private LocalTime bis;

    @Column(name = "pause_von")
    private LocalTime pauseVon;

    @Column(name = "pause_bis")
    private LocalTime pauseBis;

    @Column(name = "dauer_std")
    private Double dauerStd;

    @Column(name = "an_abwesenheit")
    private Boolean anAbwesenheit;

    @Column(name = "leistungsort")
    @Enumerated(EnumType.STRING)
    private Leistungsort leistungsort;

    @ManyToOne
    @JoinColumn(name = "seminar", referencedColumnName = "id")
    private Seminar seminar;

    @ManyToOne
    @JoinColumn(name = "zeitbuchuntyp", referencedColumnName = "id")
    private Zeitbuchungstyp zeitbuchungstyp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "leistungserfassung", referencedColumnName = "id")
    private Leistungserfassung leistungserfassung;

    @ManyToOne
    @JoinColumn(name = "kostenstelle", referencedColumnName = "id")
    private Kostenstelle kostenstelle;

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
        if (!(o instanceof Zeitbuchung that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(von, that.von) && Objects.equals(bis, that.bis) && Objects.equals(pauseVon, that.pauseVon) && Objects.equals(pauseBis, that.pauseBis) && Objects.equals(dauerStd, that.dauerStd) && Objects.equals(anAbwesenheit, that.anAbwesenheit) && Objects.equals(leistungsort, that.leistungsort) && Objects.equals(seminar, that.seminar) && Objects.equals(zeitbuchungstyp, that.zeitbuchungstyp) && Objects.equals(leistungserfassung, that.leistungserfassung) && Objects.equals(kostenstelle, that.kostenstelle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, von, bis, pauseVon, pauseBis, dauerStd, anAbwesenheit, leistungsort, seminar, zeitbuchungstyp, leistungserfassung, kostenstelle);
    }

    @Override
    public String toString() {
        return "Zeitbuchung{" +
                "id=" + id +
                ", von=" + von +
                ", bis=" + bis +
                ", pauseVon=" + pauseVon +
                ", pauseBis=" + pauseBis +
                ", dauerStd=" + dauerStd +
                ", anAbwesenheit=" + anAbwesenheit +
                ", leistungsort='" + leistungsort + '\'' +
                ", seminar=" + ((seminar != null) ? seminar.getBezeichnung() : "null") +
                ", zeitbuchuntyp=" + ((zeitbuchungstyp != null) ? zeitbuchungstyp.getType() : "null") +
                ", leistungserfassung=" + ((leistungserfassung != null) ? leistungserfassung.getId() : "null") +
                ", kostenstelle=" + ((kostenstelle != null) ? kostenstelle.getId() : "null") +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
}

