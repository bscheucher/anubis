package com.ibosng.dbservice.entities.seminar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.zeiterfassung.Zeiterfassung;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Constants.DATE_PATTERN;

@Entity
@Table(name = "seminar")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seminar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "seminar_nummer")
    private Integer seminarNummer;

    @ManyToOne
    @JoinColumn(name = "project")
    private Projekt project;

    @Column(name = "bezeichnung")
    private String bezeichnung;

    @Column(name = "identifier")
    private String identifier;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seminar_type")
    private SeminarType seminarType;

    @Column(name = "massnahmen_nr")
    private String massnahmenNr;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "start_date")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    @Column(name = "end_date")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_PATTERN)
    @Column(name = "start_time")
    private LocalTime startTime;

    @JsonSerialize(using = LocalTimeSerializer.class)
    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @OneToMany(mappedBy = "seminar", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Teilnehmer2Seminar> teilnehmerSeminars = new ArrayList<>();

    @OneToMany(mappedBy = "seminar", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Seminar2Trainer> trainerSeminars = new ArrayList<>();

    @Column(name = "standort")
    private String standort;
    
    @Column(name = "schiene_uhrzeit")
    private String schieneUhrzeit;

    @OneToMany(mappedBy = "seminar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Zeiterfassung> zeiterfassungen = new ArrayList<>();

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = LocalDateTime.now();

    @Transient
    private boolean shouldSync = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seminar seminar = (Seminar) o;
        return Objects.equals(identifier, seminar.identifier) && Objects.equals(seminarType, seminar.seminarType) && Objects.equals(project != null ? project.getId() : null, seminar.project != null ? seminar.project.getId() : null) && Objects.equals(massnahmenNr, seminar.massnahmenNr) && Objects.equals(startDate, seminar.startDate) && Objects.equals(endDate, seminar.endDate) && Objects.equals(startTime, seminar.startTime) && Objects.equals(endTime, seminar.endTime) && Objects.equals(standort, seminar.standort) && Objects.equals(schieneUhrzeit, seminar.schieneUhrzeit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, project != null ? project.getId() : null, identifier, seminarType, startDate, endDate, startTime, endTime, status, standort, schieneUhrzeit);
    }

    @Override
    public String toString() {
        return "Seminar{" +
                "id=" + id +
                ", identifier='" + identifier + '\'' +
                ", seminarType='" + seminarType + '\'' +
                ", project='" + (project != null ? project.getId() : "") + '\'' +
                ", massnahmenNr='" + massnahmenNr + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", standort='" + standort + '\'' +
                ", schieneUhrzeit='" + schieneUhrzeit + '\'' +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
}
