package com.ibosng.dbservice.repositories.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.MAFilteredResultDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MASearchCriteriaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface StammdatenRepositoryExtended {
    Page<Object[]> findAllOrderedByNachnameEintritt(Pageable pageable, String mitarbeiterType);
    Page<Object[]> findForBenutzerOrderedByNachnameEintritt(Pageable pageable, String mitarbeiterType, String benutzerEmail);

    Page<MAFilteredResultDto> findMAByCriteria(MASearchCriteriaDto maSearchCriteriaDto, Pageable pageable);
}
