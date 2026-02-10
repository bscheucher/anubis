package com.ibosng.dbservice.entities.history;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "teilnehmer_history")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeilnehmerHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nachname;
    private String vorname;
    private Integer geschlecht;

    @Column(name = "sv_nummer")
    private String svNummer;

    private LocalDate geburtsdatum;
    private Integer adresse;

    private String email;
    private Short status;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    private Character action;

    @Column(name = "action_timestamp")
    private LocalDateTime actionTimestamp;

    @Column(name = "teilnehmer_id")
    private Integer teilnehmerId;

    @Column(name = "import_filename")
    private String importFilename;

    private String info;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;


}
