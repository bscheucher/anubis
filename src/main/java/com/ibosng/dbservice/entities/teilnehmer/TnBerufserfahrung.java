package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "tn_berufserfahrung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TnBerufserfahrung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teilnehmer")
    private Teilnehmer teilnehmer;

    @ManyToOne
    @JoinColumn(name = "beruf")
    private Beruf beruf;

    @Column(name = "dauer")
    private Double dauer;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TnBerufserfahrung that = (TnBerufserfahrung) o;
        return Objects.equals(teilnehmer != null ? teilnehmer.getId() : null, that.teilnehmer != null ? that.teilnehmer.getId() : null) &&
                Objects.equals(beruf, that.beruf) &&
                Objects.equals(dauer, that.dauer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teilnehmer != null ? teilnehmer.getId() : null,
                beruf,
                dauer);
    }

    @Override
    public String toString() {
        return "TnBerufserfahrung{" +
                "teilnehmer=" + (teilnehmer != null ? teilnehmer.getId() : "null") +
                ", beruf=" + beruf +
                ", dauer=" + dauer +
                '}';
    }
}
