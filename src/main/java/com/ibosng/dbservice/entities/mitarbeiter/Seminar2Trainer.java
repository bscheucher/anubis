package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static org.springframework.data.jpa.domain.AbstractAuditable_.createdBy;

@Entity
@Table(name = "seminar_2_trainer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seminar2Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seminar_id")
    private Seminar seminar;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private Benutzer trainer;

    @Column(name = "trainer_funktion")
    private String trainerFunktion;

    @Column(name = "dienstvertrag_nr")
    private Integer dienstvertragNr;

    @Column(name = "role")
    private String role;

    @Column(name = "trainer_type")
    private Integer trainerType;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status")
    private Integer status;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Override
    public String toString() {
        return "Seminar2Trainer{" +
                "id=" + id +
                ", seminar=" + (seminar != null ? seminar.getId() : null) +
                ", trainer=" + (trainer != null ? trainer.getId() : null) +
                ", trainerFunktion=" + trainerFunktion + '\'' +
                ", dienstvertragNr=" + dienstvertragNr +
                ", role='" + role + '\'' +
                ", trainerType=" + trainerType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seminar2Trainer that = (Seminar2Trainer) o;
        return Objects.equals(seminar != null ? seminar.getId() : null, that.seminar != null ? that.seminar.getId() : null) &&
                Objects.equals(trainer != null ? trainer.getId() : null, that.trainer != null ? that.trainer.getId() : null) &&
                Objects.equals(trainerFunktion, that.trainerFunktion) &&
                Objects.equals(dienstvertragNr, that.dienstvertragNr) &&
                Objects.equals(role, that.role) &&
                Objects.equals(trainerType, that.trainerType) &&
                Objects.equals(startDate, that.startDate) &&
                Objects.equals(endDate, that.endDate) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seminar != null ? seminar.getId() : null, trainer != null ? trainer.getId() : null, dienstvertragNr, role, trainerType, startDate, endDate, status, trainerFunktion);
    }
}
