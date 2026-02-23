package com.ibosng.dbservice.entities.masterdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Entity
@Table(name = "personalnummer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Personalnummer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nummer")
    private Integer nummer;

    @Column(name = "personalnummer")
    private String personalnummer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "firma")
    private IbisFirma firma;

    @Enumerated(EnumType.STRING)
    @Column(name = "mitarbeiter_type")
    private MitarbeiterType mitarbeiterType;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @Column(name = "is_ibosng_onboarded")
    private Boolean isIbosngOnboarded = true;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "onboarded_on")
    private LocalDateTime onboardedOn;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Personalnummer{" +
                "id=" + id +
                ", nummer=" + nummer +
                ", personalnummer='" + personalnummer + '\'' +
                ", firma=" + firma +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Personalnummer that = (Personalnummer) object;
        return Objects.equals(nummer, that.nummer) &&
                Objects.equals(personalnummer, that.personalnummer) &&
                Objects.equals(firma != null ? firma.getId() : null, that.firma != null ? that.firma.getId() : null) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nummer, personalnummer, firma != null ? firma.getId() : null, status);
    }

    public boolean isIncomplete() {
        if (isNullOrBlank(this.personalnummer)) return true;
        return this.firma == null;
    }
}
