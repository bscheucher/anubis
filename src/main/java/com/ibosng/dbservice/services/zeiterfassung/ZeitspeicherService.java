package com.ibosng.dbservice.services.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.Zeitspeicher;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;
import java.util.Optional;

public interface ZeitspeicherService extends BaseService<Zeitspeicher> {
    List<Zeitspeicher> findByAbbreviation(String abbreviation);

    Optional<Zeitspeicher> findByZspNr(Integer zeitspeicherNummer);

    List<Zeitspeicher> findByZspNrIn(List<Integer> zspNummers);
}
