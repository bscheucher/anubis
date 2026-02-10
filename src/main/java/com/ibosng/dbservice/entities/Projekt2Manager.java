package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "projekt_2_manager")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Projekt2Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projekt")
    private Projekt projekt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager")
    private Benutzer manager;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "end_date")
    private LocalDate endDate;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projekt2Manager that = (Projekt2Manager) o;
        return Objects.equals(id, that.id) && Objects.equals(projekt.getId(), that.projekt.getId()) && Objects.equals(manager.getId(), that.manager.getId()) && Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projekt.getId(), manager.getId(), startDate, endDate);
    }

    @Override
    public String toString() {
        return "Projekt2Manager{" +
                "id=" + id +
                ", projekt=" + projekt.getId() +
                ", manager=" + manager.getId() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", createdOn=" + createdOn +
                '}';
    }
}
