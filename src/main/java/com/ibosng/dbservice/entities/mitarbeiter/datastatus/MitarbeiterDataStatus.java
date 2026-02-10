package com.ibosng.dbservice.entities.mitarbeiter.datastatus;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "mitarbeiter_data_status")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "data_type", discriminatorType = DiscriminatorType.STRING)
public abstract class MitarbeiterDataStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    protected Personalnummer personalnummer;

    @Column(name = "error")
    private String error;

    @Column(name = "cause")
    private String cause;

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

    @PrePersist
    @PreUpdate
    private void validate() {
        int nonNullCount = 0;
        if (this instanceof StammdatenDataStatus) {
            nonNullCount += ((StammdatenDataStatus) this).getStammdaten() != null ? 1 : 0;
        }
        if (this instanceof VertragsdatenDataStatus) {
            nonNullCount += ((VertragsdatenDataStatus) this).getVertragsdaten() != null ? 1 : 0;
        }
        if (this instanceof VordienstzeitenDataStatus) {
            nonNullCount += ((VordienstzeitenDataStatus) this).getVordienstzeiten() != null ? 1 : 0;
        }
        if (this instanceof UnterhaltsberechtigteDataStatus) {
            nonNullCount += ((UnterhaltsberechtigteDataStatus) this).getUnterhaltsberechtigte() != null ? 1 : 0;
        }
        if (nonNullCount != 1) {
            throw new IllegalStateException("Exactly one of Stammdaten, Vertragsdaten, Vordienstzeiten, or Unterhaltsberechtigte must be set.");
        }
    }

    @Override
    public String toString() {
        return "MitarbeiterDataStatus{" +
                "id=" + id +
                ", personalnummer=" + personalnummer != null ? String.valueOf(personalnummer.getId()) : "" +
                ", error='" + error + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MitarbeiterDataStatus that = (MitarbeiterDataStatus) object;
        return Objects.equals(personalnummer != null ? personalnummer.getId() : null, that.personalnummer != null ? that.personalnummer.getId() : null) &&
                Objects.equals(error, that.error) &&
                Objects.equals(cause, that.cause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalnummer != null ? personalnummer.getId() : null, error, cause);
    }
}
