package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ausschreibung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ausschreibung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "ausschreibung_nummer")
    Integer ausschreibungNummer;

    //TODO Do we need 2 bezeichnung fields for some reason?
    @Column(name = "bezeichnung")
    String bezeichnung;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    LocalDateTime createdOn = LocalDateTime.now();

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    //TODO Check this
    LocalDateTime changedOn;

    @JsonIgnore
    @Column(name = "created_by")
    String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    String changedBy;

    @Column(name = "bundesland", nullable = false)
    Integer bundesland;

    @OneToMany(mappedBy = "ausschreibung", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Projekt> projekte = new ArrayList<>();

    @Transient
    private boolean shouldSync;
}
