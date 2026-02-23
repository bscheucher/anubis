package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.Verwandtschaft;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.UnterhaltsberechtigteDataStatus;
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
@Table(name = "unterhaltsberechtigte")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Unterhaltsberechtigte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vertragsdaten_id", referencedColumnName = "id")
    private Vertragsdaten vertragsdaten;

    @Column(name = "vorname")
    private String vorname;

    @Column(name = "nachname")
    private String nachname;

    @Column(name = "svn")
    private String svn;

    @Column(name = "geburtsdatum")
    private LocalDate geburtsdatum;

    @ManyToOne
    @JoinColumn(name = " verwandtschaft", referencedColumnName = "id")
    private Verwandtschaft verwandtschaft;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MitarbeiterStatus status;

    @OneToMany(mappedBy = "unterhaltsberechtigte", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<UnterhaltsberechtigteDataStatus> errors = new ArrayList<>();

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

    @Override
    public String toString() {
        return "Unterhaltsberechtigte{" +
                "id=" + id +
                ", vertragsdaten=" + vertragsdaten != null ? String.valueOf(vertragsdaten.getId()) : " " +
                ", vorname='" + vorname + '\'' +
                ", nachname='" + nachname + '\'' +
                ", svn=" + svn +
                ", geburtsdatum=" + geburtsdatum +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Unterhaltsberechtigte that = (Unterhaltsberechtigte) object;
        return Objects.equals(vertragsdaten != null ? vertragsdaten.getId() : null, that.vertragsdaten != null ? that.vertragsdaten.getId() : null) && Objects.equals(vorname, that.vorname) && Objects.equals(nachname, that.nachname) && Objects.equals(svn, that.svn) && Objects.equals(geburtsdatum, that.geburtsdatum) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertragsdaten != null ? vertragsdaten.getId() : null, vorname, nachname, svn, geburtsdatum, status);
    }

    public void addError(String error, String cause, String createdBy) {
        UnterhaltsberechtigteDataStatus dataStatus = new UnterhaltsberechtigteDataStatus();
        dataStatus.setUnterhaltsberechtigte(this);
        dataStatus.setError(error);
        dataStatus.setCause(cause);
        dataStatus.setCreatedBy(createdBy);
        this.errors.add(dataStatus);
    }

    public void setErrors(List<UnterhaltsberechtigteDataStatus> newErrors) {
        // Clear the existing errors while maintaining orphan removal
        this.errors.clear();
        if (newErrors != null) {
            for (UnterhaltsberechtigteDataStatus error : newErrors) {
                error.setUnterhaltsberechtigte(this);
            }
            this.errors.addAll(newErrors);
        }
    }
}
