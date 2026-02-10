package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.entities.reports.ReportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface ReportRepository extends JpaRepository<Report, Integer> {

    Report findByReportName(String reportName);

    List<Report> findAllByReportType(ReportType reportType);
}
