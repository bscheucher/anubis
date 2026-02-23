package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.reports.Report;
import com.ibosng.dbservice.entities.reports.ReportParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface ReportParameterRepository extends JpaRepository<ReportParameter, Integer> {

    List<ReportParameter> findAllByReport(Report report);
}
