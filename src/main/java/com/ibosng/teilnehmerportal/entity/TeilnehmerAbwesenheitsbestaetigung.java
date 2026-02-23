package com.ibosng.teilnehmerportal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "teilnehmer_abwesenheitsbestaetigung")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeilnehmerAbwesenheitsbestaetigung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String vorname;

    @Column(nullable = false)
    private String nachname;

    @Column(name = "sv_nummer", nullable = false, length = 10)
    private String svNummer;

    @Column(name = "start_datum", nullable = false)
    private LocalDate startDatum;

    @Column(name = "end_datum", nullable = false)
    private LocalDate endDatum;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}