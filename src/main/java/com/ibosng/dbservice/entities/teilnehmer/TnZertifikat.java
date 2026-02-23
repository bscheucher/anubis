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
@Table(name = "tn_zertifikat")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TnZertifikat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teilnehmer")
    private Teilnehmer teilnehmer;

    @Column(name = "bezeichnung")
    private String bezeichnung;

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
        TnZertifikat that = (TnZertifikat) o;
        return Objects.equals(teilnehmer != null ? teilnehmer.getId() : null, that.teilnehmer != null ? that.teilnehmer.getId() : null) &&
                Objects.equals(bezeichnung, that.bezeichnung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teilnehmer != null ? teilnehmer.getId() : null, bezeichnung);
    }

    @Override
    public String toString() {
        return "TnZertifikat{" +
                "teilnehmer=" + (teilnehmer != null ? teilnehmer.getId() : "null") +
                ", bezeichnung='" + bezeichnung + '\'' +
                '}';
    }
}
