package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.ZusatzInfo;
import com.ibosng.dbservice.entities.masterdata.*;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.MitarbeiterDataStatus;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.StammdatenDataStatus;
import com.ibosng.dbservice.entities.telefon.Telefon;
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
@Table(name = "stammdaten")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Stammdaten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @OneToMany(mappedBy = "stammdaten", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StammdatenDataStatus> errors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "anrede", referencedColumnName = "id")
    private Anrede anrede;

    @ManyToOne
    @JoinColumn(name = "titel", referencedColumnName = "id")
    private Titel titel;

    @ManyToOne
    @JoinColumn(name = "titel2", referencedColumnName = "id")
    private Titel titel2;

    private String nachname;

    private String vorname;

    private String geburtsname;

    private String svnr;

    @Enumerated(EnumType.STRING)
    @Column(name = "ecard_status")
    private BlobStatus ecardStatus;

    @ManyToOne
    @JoinColumn(name = "geschlecht", referencedColumnName = "id")
    private Geschlecht geschlecht;

    @ManyToOne
    @JoinColumn(name = "familienstand", referencedColumnName = "id")
    private Familienstand familienstand;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "geburtsdatum")
    private LocalDate geburtsdatum;

    private Integer lebensalter;

    @ManyToOne
    @JoinColumn(name = "staatsbuergerschaft", referencedColumnName = "id")
    private Land staatsbuergerschaft;

    @ManyToOne
    @JoinColumn(name = "muttersprache", referencedColumnName = "id")
    private Muttersprache muttersprache;

    @ManyToOne
    @JoinColumn(name = "adresse", referencedColumnName = "id")
    private Adresse adresse;

    @ManyToOne
    @JoinColumn(name = "abweichende_adresse", referencedColumnName = "id")
    private Adresse abweichendeAdresse;

    private String email;

    @ManyToOne
    @JoinColumn(name = "mobilnummer", referencedColumnName = "id")
    private Telefon mobilnummer;

    @Column(name = "handy_signatur")
    private boolean handySignatur;

    @ManyToOne
    @JoinColumn(name = "bank", referencedColumnName = "id")
    private BankDaten bank;

    @OneToOne
    @JoinColumn(name = "zusatz_info", referencedColumnName = "id")
    private ZusatzInfo zusatzInfo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MitarbeiterStatus status;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    public void addError(String error, String cause, String createdBy) {
        StammdatenDataStatus dataStatus = new StammdatenDataStatus();
        dataStatus.setPersonalnummer(this.personalnummer);
        dataStatus.setStammdaten(this);
        dataStatus.setError(error);
        dataStatus.setCause(cause);
        dataStatus.setCreatedBy(createdBy);
        this.errors.add(dataStatus);
    }

    public void setErrors(List<StammdatenDataStatus> newErrors) {
        // Clear the existing errors while maintaining orphan removal
        this.errors.clear();
        if (newErrors != null) {
            for (StammdatenDataStatus error : newErrors) {
                error.setStammdaten(this);
            }
            this.errors.addAll(newErrors);
        }
    }

    @Override
    public String toString() {
        return "Stammdaten{" +
                "personalnummer='" + personalnummer.getId() + '\'' +
                ", errors=" + errors.stream().map(MitarbeiterDataStatus::getError).toList() +
                ", anrede=" + anrede.getName() +
                ", titel=" + titel.getName() +
                ", titel2=" + titel2.getName() +
                ", nachname='" + nachname + '\'' +
                ", vorname='" + vorname + '\'' +
                ", geburtsname='" + geburtsname + '\'' +
                ", svnr=" + svnr +
                ", ecard='" + ecardStatus + '\'' +
                ", abweichendeAdresse='" + abweichendeAdresse != null ? String.valueOf(abweichendeAdresse.getId()) : " " + '\'' +
                ", geschlecht='" + geschlecht.getName() + '\'' +
                ", familienstand=" + familienstand.getName() +
                ", geburtsdatum=" + geburtsdatum +
                ", lebensalter=" + lebensalter +
                ", staatsbuergerschaft=" + staatsbuergerschaft.getId() +
                ", muttersprache='" + muttersprache + '\'' +
                ", adresse=" + adresse.getId() +
                ", email='" + email + '\'' +
                ", mobilnummer=" + mobilnummer.getId() +
                ", bank=" + bank.getId() +
                ", zusatzInfo='" + zusatzInfo != null ? String.valueOf(zusatzInfo.getId()) : "" + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Stammdaten that = (Stammdaten) object;
        return Objects.equals(personalnummer != null ? personalnummer.getId() : null, that.personalnummer != null ? that.personalnummer.getId() : null) &&
                Objects.equals(anrede != null ? anrede.getId() : null, that.anrede != null ? that.anrede.getId() : null) &&
                Objects.equals(titel != null ? titel.getId() : null, that.titel != null ? that.titel.getId() : null) &&
                Objects.equals(titel2 != null ? titel2.getId() : null, that.titel2 != null ? that.titel2.getId() : null) &&
                Objects.equals(nachname, that.nachname) &&
                Objects.equals(vorname, that.vorname) &&
                Objects.equals(geburtsname, that.geburtsname) &&
                Objects.equals(svnr, that.svnr) &&
                Objects.equals(ecardStatus, that.ecardStatus) &&
                Objects.equals(geschlecht != null ? geschlecht.getId() : null, that.geschlecht != null ? that.geschlecht.getId() : null) &&
                Objects.equals(familienstand != null ? familienstand.getId() : null, that.familienstand != null ? that.familienstand.getId() : null) &&
                Objects.equals(geburtsdatum, that.geburtsdatum) &&
                Objects.equals(lebensalter, that.lebensalter) &&
                Objects.equals(staatsbuergerschaft != null ? staatsbuergerschaft.getId() : null, that.staatsbuergerschaft != null ? that.staatsbuergerschaft.getId() : null) &&
                Objects.equals(muttersprache, that.muttersprache) &&
                Objects.equals(adresse != null ? adresse.getId() : null, that.adresse != null ? that.adresse.getId() : null) &&
                Objects.equals(abweichendeAdresse != null ? abweichendeAdresse.getId() : null, that.abweichendeAdresse != null ? that.abweichendeAdresse.getId() : null) &&
                Objects.equals(email, that.email) &&
                Objects.equals(mobilnummer != null ? mobilnummer.getId() : null, that.mobilnummer != null ? that.mobilnummer.getId() : null) &&
                Objects.equals(bank != null ? bank.getId() : null, that.bank != null ? that.bank.getId() : null) &&
                Objects.equals(zusatzInfo != null ? zusatzInfo.getId() : null, that.zusatzInfo != null ? that.zusatzInfo.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                personalnummer != null ? personalnummer.getId() : null,
                anrede != null ? anrede.getId() : null,
                titel != null ? titel.getId() : null,
                titel2 != null ? titel2.getId() : null,
                nachname,
                vorname,
                geburtsname,
                svnr,
                ecardStatus,
                geschlecht != null ? geschlecht.getId() : null,
                familienstand != null ? familienstand.getId() : null,
                geburtsdatum,
                lebensalter,
                staatsbuergerschaft != null ? staatsbuergerschaft.getId() : null,
                muttersprache,
                adresse != null ? adresse.getId() : null,
                abweichendeAdresse != null ? abweichendeAdresse.getId() : null,
                email,
                mobilnummer != null ? mobilnummer.getId() : null,
                bank != null ? bank.getId() : null,
                zusatzInfo != null ? zusatzInfo.getId() : null
        );
    }

}
