package com.ibosng.dbservice.entities.natif;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "kompetenz")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kompetenz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teilnehmer_id", nullable = false)
    private Teilnehmer teilnehmer;

    @Column(name = "art")
    private String art;

    @Column(name = "name")
    private String name;

    @Column(name = "confidence_name")
    private Double confidenceName;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "confidence_score")
    private Double confidenceScore;

    @Column(name = "pin_code")
    private String pinCode;

    @Column(name = "confidence_pin_code")
    private Double confidencePinCode;

    @Column(name = "tagesdatum")
    private LocalDate tagesdatum;

    @Column(name = "confidence_tagesdatum")
    private Double confidenceTagesdatum;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Kompetenz{" +
                "id=" + id +
                ", teilnehmer=" + teilnehmer +
                ", art='" + art + '\'' +
                ", name='" + name + '\'' +
                ", confidenceName=" + confidenceName +
                ", score=" + score +
                ", confidenceScore=" + confidenceScore +
                ", pinCode='" + pinCode + '\'' +
                ", confidencePinCode=" + confidencePinCode +
                ", tagesdatum=" + tagesdatum +
                ", confidenceTagesdatum=" + confidenceTagesdatum +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kompetenz kompetenz = (Kompetenz) o;
        return Objects.equals(id, kompetenz.id) && Objects.equals(teilnehmer, kompetenz.teilnehmer) &&
                Objects.equals(art, kompetenz.art) && Objects.equals(name, kompetenz.name) &&
                Objects.equals(confidenceName, kompetenz.confidenceName) && Objects.equals(score, kompetenz.score) &&
                Objects.equals(confidenceScore, kompetenz.confidenceScore) &&
                Objects.equals(pinCode, kompetenz.pinCode) &&
                Objects.equals(confidencePinCode, kompetenz.confidencePinCode) &&
                Objects.equals(tagesdatum, kompetenz.tagesdatum) &&
                Objects.equals(confidenceTagesdatum, kompetenz.confidenceTagesdatum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teilnehmer, art, name, confidenceName, score, confidenceScore, pinCode,
                confidencePinCode, tagesdatum, confidenceTagesdatum);
    }
}
