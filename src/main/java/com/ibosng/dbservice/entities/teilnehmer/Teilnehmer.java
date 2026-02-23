package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.masterdata.Anrede;
import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import com.ibosng.dbservice.entities.masterdata.Muttersprache;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.natif.Kompetenz;
import com.ibosng.dbservice.entities.seminar.Seminar;
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
import java.util.Set;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "teilnehmer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teilnehmer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "titel")
    private String titel;

    @Column(name = "titel2")
    private String titel2;

    @Column(name = "vorname")
    private String vorname;

    @Column(name = "nachname")
    private String nachname;

    @ManyToOne
    @JoinColumn(name = "geschlecht")
    private Geschlecht geschlecht;

    @Column(name = "sv_nummer")
    private String svNummer;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "geburtsdatum")
    private LocalDate geburtsdatum;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TeilnehmerStatus status;

    @OneToMany(mappedBy = "teilnehmer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TeilnehmerDataStatus> errors = new ArrayList<>();

    @Column(name = "import_filename")
    private String importFilename;

    @Column(name = "info")
    private String info;

    @Column(name = "is_ueba")
    private boolean isUeba;

    @Column(name = "has_bis_document")
    private boolean hasBisDocument;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "teilnehmer_nations",
            joinColumns = @JoinColumn(name = "teilnehmer_id"),
            inverseJoinColumns = @JoinColumn(name = "land_id"))
    private Set<Land> nation;

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

    @OneToMany(mappedBy = "teilnehmer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeilnehmerTelefon> teilnehmerTelefons = new ArrayList<>();

    @OneToMany(mappedBy = "teilnehmer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Teilnehmer2Seminar> teilnehmerSeminars = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "adresse")
    private Adresse adresse;

    @ManyToOne
    @JoinColumn(name = "anrede", referencedColumnName = "id")
    private Anrede anrede;

    @OneToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ursprung", referencedColumnName = "id")
    private Adresse ursprung;

    @Column(name = "ziel")
    private String ziel;

    @Column(name = "vermittelbar_ab")
    private LocalDate vermittelbarAb;

    @Column(name = "vermittlungs_notiz")
    private String vermittlungsNotiz;

    @Column(name = "vermittelbar_ausserhalb_ams")
    private boolean vermittelbarAusserhalbAms;

    @ManyToOne
    @JoinColumn(name = "muttersprache")
    private Muttersprache muttersprache;

    @OneToMany(mappedBy = "teilnehmer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Teilnehmer2Wunschberuf> teilnehmer2Wunschberufe = new ArrayList<>();

    @OneToMany(mappedBy = "teilnehmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Kompetenz> kompetenzen = new ArrayList<>();

    @OneToMany(mappedBy = "teilnehmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TnPraktika> praktika = new ArrayList<>();

    @OneToMany(mappedBy = "teilnehmer", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TnAbschluss> abschluesse = new ArrayList<>();

    @Transient
    private boolean shouldSync = true;

    public Teilnehmer(String titel,
                      String titel2,
                      String vorname,
                      String nachname,
                      Geschlecht geschlecht,
                      String svNummer,
                      LocalDate geburtsdatum,
                      String email,
                      TeilnehmerStatus status,
                      String importFilename, String
                              createdBy,
                      List<Telefon> telefons,
                      Adresse adresse) {
        this.titel = titel;
        this.titel2 = titel2;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geschlecht = geschlecht;
        this.svNummer = svNummer;
        this.geburtsdatum = geburtsdatum;
        this.email = email;
        this.status = status;
        this.importFilename = importFilename;
        this.createdBy = createdBy;
        addTelefons(telefons);
        this.adresse = adresse;
    }

    public void setTelefons(List<Telefon> telefons) {
        setTeilnehmerTelefons(new ArrayList<>());
        addTelefons(telefons);
    }

    public void addTelefons(List<Telefon> telefons) {
        for (Telefon telefon : telefons) {
            addTelefon(telefon);
        }

    }

    public void addTelefon(Telefon telefon) {
        TeilnehmerTelefon teilnehmerTelefon = new TeilnehmerTelefon();
        teilnehmerTelefon.setTeilnehmer(this);
        teilnehmerTelefon.setTelefon(telefon);
        getTeilnehmerTelefons().add(teilnehmerTelefon);
    }

    public List<Telefon> getTelefons() {
        return getTeilnehmerTelefons().stream().map(TeilnehmerTelefon::getTelefon).toList();
    }

    public void setSeminars(List<Seminar> seminars) {
        setTeilnehmerSeminars(new ArrayList<>());
        addSeminars(seminars);
    }

    public void addSeminars(List<Seminar> seminars) {
        for (Seminar seminar : seminars) {
            addSeminar(seminar);
        }

    }

    public Teilnehmer2Seminar addSeminar(Seminar seminar) {
        Teilnehmer2Seminar teilnehmer2Seminar = new Teilnehmer2Seminar();
        teilnehmer2Seminar.setStatus(TeilnehmerStatus.NEW);
        teilnehmer2Seminar.setTeilnehmer(this);
        teilnehmer2Seminar.setSeminar(seminar);
        getTeilnehmerSeminars().add(teilnehmer2Seminar);
        return teilnehmer2Seminar;
    }

    public List<Seminar> getSeminars() {
        return getTeilnehmerSeminars().stream().map(Teilnehmer2Seminar::getSeminar).toList();
    }

    @Override
    public String toString() {
        return "Teilnehmer{" +
                ", titel='" + titel + '\'' +
                ", titel2='" + titel2 + '\'' +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", geschlecht='" + geschlecht + '\'' +
                ", svNummer=" + svNummer +
                ", geburtsdatum=" + geburtsdatum +
                ", email='" + email + '\'' +
                ", ursprung='" + (ursprung != null ? ursprung.getId() : "null") + '\'' +
                ", ziel='" + ziel + '\'' +
                ", vermittelbarAb=" + vermittelbarAb +
                ", vermittlungsNotiz='" + vermittlungsNotiz + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teilnehmer that = (Teilnehmer) o;
        return Objects.equals(vorname, that.vorname) &&
                Objects.equals(nachname, that.nachname) &&
                Objects.equals(geschlecht != null ? geschlecht.getId() : null, that.geschlecht != null ? that.geschlecht.getId() : null) &&
                Objects.equals(svNummer, that.svNummer) &&
                Objects.equals(geburtsdatum, that.geburtsdatum) &&
                Objects.equals(ziel, that.ziel) &&
                Objects.equals(vermittelbarAb, that.vermittelbarAb) &&
                Objects.equals(vermittlungsNotiz, that.vermittlungsNotiz) &&
                Objects.equals(ursprung != null ? ursprung.getId() : null,
                        that.ursprung != null ? that.ursprung.getId() : null);

    }

    @Override
    public int hashCode() {
        return Objects.hash(vorname, nachname, geschlecht != null ? geschlecht.getId() : null, svNummer, geburtsdatum, email, status, errors, importFilename, createdOn, createdBy, changedBy, adresse != null ? adresse.getId() : null, ursprung != null ? ursprung.getId() : null, ziel, vermittelbarAb, vermittlungsNotiz);
    }

    public void addError(String error, String cause, String createdBy) {
        TeilnehmerDataStatus dataStatus = new TeilnehmerDataStatus();
        dataStatus.setError(error);
        dataStatus.setCause(cause);
        dataStatus.setTeilnehmer(this);
        dataStatus.setCreatedBy(createdBy);
        dataStatus.setCreatedOn(getLocalDateNow());
        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }
        this.errors.add(dataStatus);
    }

    public void setErrors(List<TeilnehmerDataStatus> newErrors) {
        // Clear the existing errors while maintaining orphan removal
        this.errors.clear();
        if (newErrors != null) {
            for (TeilnehmerDataStatus error : newErrors) {
                error.setTeilnehmer(this);
                this.errors.add(error);
            }
        }
    }
}