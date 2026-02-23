package com.ibosng.dbservice.entities.mitarbeiter;

import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Dienstort;
import com.ibosng.dbservice.entities.IbosReference;
import com.ibosng.dbservice.entities.masterdata.*;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.MitarbeiterDataStatus;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VertragsdatenDataStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "vertragsdaten")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vertragsdaten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @ManyToOne
    @JoinColumn(name = "kostenstelle", referencedColumnName = "id")
    private Kostenstelle kostenstelle;

    @OneToMany(mappedBy = "vertragsdaten", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<VertragsdatenDataStatus> errors = new ArrayList<>();

    @Column(name = "eintritt")
    private LocalDate eintritt;

    @Column(name = "is_befristet")
    private Boolean isBefristet;

    @Column(name = "befristung_bis")
    private LocalDate befristungBis;

    @ManyToOne
    @JoinColumn(name = "dienstort", referencedColumnName = "id")
    private Dienstort dienstort;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fuehrungskraft", referencedColumnName = "id")
    private Benutzer fuehrungskraft;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "startcoach", referencedColumnName = "id")
    private Benutzer startcoach;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fuehrungskraft_ref", referencedColumnName = "id")
    private IbosReference fuehrungskraftRef;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "startcoach_ref", referencedColumnName = "id")
    private IbosReference startcoachRef;

    @ManyToOne
    @JoinColumn(name = "kategorie", referencedColumnName = "id")
    private Kategorie kategorie;

    @ManyToOne
    @JoinColumn(name = "taetigkeit", referencedColumnName = "id")
    private Taetigkeit taetigkeit;

    @ManyToOne
    @JoinColumn(name = "job_bezeichnung", referencedColumnName = "id")
    private Jobbeschreibung jobBezeichnung;

    @Column(name = "notiz_allgemein")
    private String notizAllgemein;

    @Column(name = "mobile_working")
    private Boolean mobileWorking;

    @Column(name = "weitere_adresse_zu_hauptwohnsitz")
    private Boolean weitereAdressezuHauptwohnsitz;

    @Column(name = "notiz_zusatz_vereinbarung")
    private String notizZusatzvereinbarung;

    @ManyToOne
    @JoinColumn(name = "klasse", referencedColumnName = "id")
    private Klasse klasse;

    @ManyToOne
    @JoinColumn(name = "adresse", referencedColumnName = "id")
    private Adresse adresse;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MitarbeiterStatus status;

    @ManyToOne
    @JoinColumn(name = "abrechnungsgruppe", referencedColumnName = "id")
    private Abrechnungsgruppe abrechnungsgruppe;

    @ManyToOne
    @JoinColumn(name = "kommunalsteuergemeinde", referencedColumnName = "id")
    private Kommunalsteuergemeinde kommunalsteuergemeinde;

    @ManyToOne
    @JoinColumn(name = "dienstnehmergruppe", referencedColumnName = "id")
    private Dienstnehmergruppe dienstnehmergruppe;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    public void addError(String error, String cause, String createdBy) {
        VertragsdatenDataStatus dataStatus = new VertragsdatenDataStatus();
        dataStatus.setPersonalnummer(personalnummer);
        dataStatus.setVertragsdaten(this);
        dataStatus.setError(error);
        dataStatus.setCause(cause);
        dataStatus.setCreatedBy(createdBy);
        this.errors.add(dataStatus);
    }

    public void setErrors(List<VertragsdatenDataStatus> newErrors) {
        // Clear the existing errors while maintaining orphan removal
        this.errors.clear();
        if (newErrors != null) {
            for (VertragsdatenDataStatus error : newErrors) {
                error.setVertragsdaten(this);
            }
            this.errors.addAll(newErrors);
        }
    }

    @Override
    public String toString() {
        return "Vertragsdaten{" +
                "personalnummer='" + personalnummer.getId() + '\'' +
                ", errors=" + errors.stream().map(MitarbeiterDataStatus::getError).toList() +
                ", eintritt=" + eintritt +
                ", befristungVon=" + isBefristet +
                ", befristungBis=" + befristungBis +
                ", dienstort='" + dienstort + '\'' +
                ", kostenstelle='" + kostenstelle != null ? String.valueOf(kostenstelle.getId()) : "" + '\'' +
                ", fuerhrungskraft=" + fuehrungskraft +
                ", startcoach=" + startcoach +
                ", kategorie=" + kategorie.getId() +
                ", taetigkeit=" + taetigkeit.getId() +
                ", jobBezeichnung=" + jobBezeichnung.getId() +
                ", notizAllgemein='" + notizAllgemein + '\'' +
                ", mobileWorking=" + mobileWorking +
                ", weitereAdressezuHauptwohnsitz=" + weitereAdressezuHauptwohnsitz +
                ", adresse='" + adresse.toString() + '\'' +
                ", status=" + status +
                ", klasse=" + klasse != null ? String.valueOf(klasse.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertragsdaten that = (Vertragsdaten) o;
        return id.equals(that.id) &&
                personalnummer.equals(that.personalnummer) &&
                Objects.equals(eintritt, that.eintritt) &&
                Objects.equals(isBefristet, that.isBefristet) &&
                Objects.equals(befristungBis, that.befristungBis) &&
                Objects.equals(dienstort, that.dienstort) &&
                Objects.equals(kostenstelle != null ? kostenstelle.getId() : null, that.kostenstelle != null ? that.kostenstelle.getId() : null) &&
                Objects.equals(fuehrungskraft, that.fuehrungskraft) &&
                Objects.equals(startcoach, that.startcoach) &&
                Objects.equals(kategorie != null ? kategorie.getId() : null, that.kategorie != null ? that.kategorie.getId() : null) &&
                Objects.equals(taetigkeit != null ? taetigkeit.getId() : null, that.taetigkeit != null ? that.taetigkeit.getId() : null) &&
                Objects.equals(jobBezeichnung != null ? jobBezeichnung.getId() : null, that.jobBezeichnung != null ? that.jobBezeichnung.getId() : null) &&
                Objects.equals(notizAllgemein, that.notizAllgemein) &&
                Objects.equals(mobileWorking, that.mobileWorking) &&
                Objects.equals(weitereAdressezuHauptwohnsitz, that.weitereAdressezuHauptwohnsitz) &&
                Objects.equals(adresse != null ? adresse.getId() : null, that.adresse != null ? that.adresse.getId() : null) &&
                status == that.status &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(changedBy, that.changedBy) &&
                Objects.equals(changedOn, that.changedOn) &&
                Objects.equals(klasse != null ? klasse.getId() : null, that.klasse != null ? that.klasse.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personalnummer != null ? personalnummer.getId() : null,
                eintritt,
                isBefristet,
                befristungBis,
                dienstort,
                kostenstelle != null ? kostenstelle.getId() : null,
                fuehrungskraft,
                startcoach,
                kategorie != null ? kategorie.getId() : null,
                klasse != null ? klasse.getId() : null,
                taetigkeit != null ? taetigkeit.getId() : null,
                jobBezeichnung != null ? jobBezeichnung.getId() : null,
                notizAllgemein,
                mobileWorking,
                weitereAdressezuHauptwohnsitz,
                adresse != null ? adresse.getId() : null,
                status,
                createdOn,
                createdBy,
                changedBy,
                changedOn);
    }
}
