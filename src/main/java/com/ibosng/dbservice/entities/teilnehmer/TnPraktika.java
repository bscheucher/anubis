package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "tn_praktika")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TnPraktika {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teilnehmer")
    private Teilnehmer teilnehmer;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "erprobung")
    private String erprobung;

    @Column(name = "ergebnis")
    private String ergebnis;

    @Column(name = "notiz")
    private String notiz;

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
        TnPraktika that = (TnPraktika) o;
        return Objects.equals(teilnehmer != null ? teilnehmer.getId() : null,
                that.teilnehmer != null ? that.teilnehmer.getId() : null) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teilnehmer != null ? teilnehmer.getId() : null, startDate, endDate);
    }

    @Override
    public String toString() {
        return "TnPraktika{" +
                "id=" + id +
                ", teilnehmer=" + (teilnehmer != null ? teilnehmer.getId() : "null") +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", erprobung='" + erprobung + '\'' +
                ", ergebnis='" + ergebnis + '\'' +
                ", notiz='" + notiz + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
}
