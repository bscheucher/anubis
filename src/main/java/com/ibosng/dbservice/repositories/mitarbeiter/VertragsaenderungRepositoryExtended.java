package com.ibosng.dbservice.repositories.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungOverviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface VertragsaenderungRepositoryExtended {

    Page<VertragsaenderungOverviewDto> findAllOrderedAndFilteredForOverview(String searchTerm, List<String> statuses, Pageable pageable);
}
