package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VordienstzeitenService extends BaseService<Vordienstzeiten> {

    Optional<Vordienstzeiten> findByPersonalnummer(String personalnummer);

    List<Vordienstzeiten> findAllByVertragsdatenId(Integer vertragsdatenId);

    VordienstzeitenDto mapVordienstzeitenToDto(Vordienstzeiten vordienstzeiten);

    List<Vordienstzeiten> findAllByCreatedOnOrChangedOnAfter(LocalDateTime after);
}
