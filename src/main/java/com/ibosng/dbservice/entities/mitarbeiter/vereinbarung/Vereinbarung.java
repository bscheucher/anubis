package com.ibosng.dbservice.entities.mitarbeiter.vereinbarung;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "vereinbarung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vereinbarung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "vereinbarung_name")
    String vereinbarungName;

    @Column(name = "vereinbarung_file")
    String vereinbarungFile;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wworkflow_id", unique = true)
    private WWorkflow workflow;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "personalnummer")
    private Personalnummer personalnummer;

    @Column(name = "fuehrungskraft")
    String fuehrungskraft;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VereinbarungStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "firma")
    private IbisFirma firma;

    @OneToMany(mappedBy = "vereinbarung", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<VereinbarungReportParameter> parameters = new ArrayList<>();

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "gueltig_ab")
    private LocalDate gueltigAb;

    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "gueltig_bis")
    private LocalDate gueltigBis;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;
}
