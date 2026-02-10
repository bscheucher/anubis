package com.ibosng.dbservice.entities.reports;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "report")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "report_name")
    String reportName;

    //TODO Rework this to file-share folder and propagate changes for upload/download logic in other services
    @Column(name = "source_path")
    String sourcePath;

    @Column(name = "main_report_file")
    String mainReportFile;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_source")
    private ReportDataSource dataSource;

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type")
    private ReportType reportType;

    @OneToMany(mappedBy = "report", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ReportParameter> reportParameters = new ArrayList<>();

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

}
