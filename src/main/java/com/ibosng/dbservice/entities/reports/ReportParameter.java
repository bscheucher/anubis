package com.ibosng.dbservice.entities.reports;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "report_parameter")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportParameter {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ReportParamType type;

    @Column(name = "description")
    private String description;

    @Column(name = "default_value")
    private String defaultValue;

    @Column(name = "label")
    private String label;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "report_id")
    private Report report;

    @Column(name = "required")
    private Boolean required;

    @Override
    public String toString() {
        return "ReportParameter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", label=" + label +
                ", required=" + required +
                ", defaultValue=" + defaultValue +
                ", description='" + description + '\'' +
                ", reportId=" + report.getId() +
                ", reportName=" + report.getReportName() +
                '}';
    }
}
