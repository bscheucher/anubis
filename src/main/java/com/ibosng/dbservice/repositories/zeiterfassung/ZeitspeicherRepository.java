package com.ibosng.dbservice.repositories.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.Zeitspeicher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface ZeitspeicherRepository  extends JpaRepository<Zeitspeicher, Integer> {

    List<Zeitspeicher> findByAbbreviation(String abbreviation);
    Optional<Zeitspeicher> findByZeitspeicherNummer(Integer zeispeicherNummer);

    List<Zeitspeicher> findByZeitspeicherNummerIn(Collection<Integer> zeitspeicherNummers);
}
