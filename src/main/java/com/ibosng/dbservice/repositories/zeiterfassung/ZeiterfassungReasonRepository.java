package com.ibosng.dbservice.repositories.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZeiterfassungReasonRepository extends JpaRepository<ZeiterfassungReason, Integer> {
    Optional<ZeiterfassungReason> findByBezeichnungAndShortBezeichnung(String bezeichnung, String shortBezeichnung);
    Optional<ZeiterfassungReason> findByBezeichnungIgnoreCaseAndShortBezeichnungIgnoreCase(String bezeichnung, String shortBezeichnung);
    ZeiterfassungReason findByBezeichnung(String bezeichnung);
    List<ZeiterfassungReason> findAllByIbosId(Integer ibosId);
    List<ZeiterfassungReason> findAllByIbosIdAndBezeichnung(Integer ibosId, String bezeichnung);
    List<ZeiterfassungReason> findAllByIbosIdAndShortBezeichnung(Integer ibosId, String shortBezeichnung);

    @Query("select distinct r.ibosId from ZeiterfassungReason r where r.ibosId is not null")
    List<Integer> findAllReasonsWithIbosidNotNull();
}
