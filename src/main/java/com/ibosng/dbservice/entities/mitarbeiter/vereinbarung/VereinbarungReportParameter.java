package com.ibosng.dbservice.entities.mitarbeiter.vereinbarung;

import com.ibosng.dbservice.entities.reports.ReportParamType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vereinbarung_report_parameter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VereinbarungReportParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vereinbarung_id")
    private Vereinbarung vereinbarung;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReportParamType type;
}

