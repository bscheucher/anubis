package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.Vertragsart;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.MitarbeiterDataStatus;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VordienstzeitenDataStatus;
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
@Table(name = "vordienstzeiten")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vordienstzeiten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "vordienstzeiten", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<VordienstzeitenDataStatus> errors = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vertragsdaten_id", referencedColumnName = "id")
    private Vertragsdaten vertragsdaten;

    @ManyToOne
    @JoinColumn(name = "vertragsart", referencedColumnName = "id")
    private Vertragsart vertragsart;

    @Column(name = "firma")
    private String firma;

    @Column(name = "von")
    private LocalDate von;

    @Column(name = "bis")
    private LocalDate bis;

    @Column(name = "wochenstunden")
    private Double wochenstunden;

    @Column(name = "anrechenbar")
    private boolean anrechenbar = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "nachweis_status")
    private BlobStatus nachweisStatus;

    @Column(name = "nachweis_file_name")
    private String nachweisFileName;

    @Column(name = "facheinschlaegig")
    private Double facheinschlaegig;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MitarbeiterStatus status;

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

    public void addError(String error, String cause, String createdBy) {
        VordienstzeitenDataStatus dataStatus = new VordienstzeitenDataStatus();
        dataStatus.setVordienstzeiten(this);
        dataStatus.setError(error);
        dataStatus.setCause(cause);
        dataStatus.setCreatedBy(createdBy);
        this.errors.add(dataStatus);
    }

    public void setErrors(List<VordienstzeitenDataStatus> newErrors) {
        // Clear the existing errors while maintaining orphan removal
        this.errors.clear();
        if (newErrors != null) {
            for (VordienstzeitenDataStatus error : newErrors) {
                error.setVordienstzeiten(this);
            }
            this.errors.addAll(newErrors);
        }
    }

    @Override
    public String toString() {
        return "Vordienstzeiten{" +
                "id=" + id +
                ", vertragsdaten=" + vertragsdaten != null ? String.valueOf(vertragsdaten.getId()) : "" +
                ", vertragsart=" + vertragsart != null ? String.valueOf(vertragsart.getId()) : "" +
                ", errors=" + errors.stream().map(MitarbeiterDataStatus::getError).toList() +
                ", firma='" + firma + '\'' +
                ", von=" + von +
                ", bis=" + bis +
                ", wochenstunden=" + wochenstunden +
                ", anrechenbar=" + anrechenbar +
                ", nachweisStatus='" + nachweisStatus + '\'' +
                ", nachweisFilename='" + nachweisFileName + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Vordienstzeiten that = (Vordienstzeiten) object;
        return Objects.equals(vertragsdaten != null ? vertragsdaten.getId() : null, that.vertragsdaten != null ? that.vertragsdaten.getId() : null) &&
                Objects.equals(vertragsart != null ? vertragsart.getId() : null, that.vertragsart != null ? that.vertragsart.getId() : null) &&
                Objects.equals(firma, that.firma) &&
                Objects.equals(von, that.von) &&
                Objects.equals(bis, that.bis) &&
                Objects.equals(wochenstunden, that.wochenstunden) &&
                Objects.equals(anrechenbar, that.anrechenbar) &&
                Objects.equals(nachweisStatus, that.nachweisStatus) &&
                Objects.equals(nachweisFileName, that.nachweisFileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertragsdaten != null ? vertragsdaten.getId() : null,
                vertragsart != null ? vertragsart.getId() : null,
                firma,
                von,
                bis,
                wochenstunden,
                anrechenbar,
                nachweisStatus,
                nachweisFileName);
    }

    public Vordienstzeiten(Vertragsdaten vertragsdaten, Vertragsart vertragsart, String firma, LocalDate von, LocalDate bis, Double wochenstunden, boolean anrechenbar, BlobStatus nachweisStatus, MitarbeiterStatus status, String createdBy) {
        this.vertragsdaten = vertragsdaten;
        this.vertragsart = vertragsart;
        this.firma = firma;
        this.von = von;
        this.bis = bis;
        this.wochenstunden = wochenstunden;
        this.anrechenbar = anrechenbar;
        this.nachweisStatus = nachweisStatus;
        this.status = status;
        this.createdBy = createdBy;
    }
}
