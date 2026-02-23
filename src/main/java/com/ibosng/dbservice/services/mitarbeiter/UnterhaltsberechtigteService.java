package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDateTime;
import java.util.List;

public interface UnterhaltsberechtigteService extends BaseService<Unterhaltsberechtigte> {
    List<Unterhaltsberechtigte> findAllByVertragsdatenId(Integer vertragsdatenId);

    UnterhaltsberechtigteDto mapUnterhaltsberechtigteToDto(Unterhaltsberechtigte unterhaltsberechtigte);

    List<Unterhaltsberechtigte> findAllByCreatedOnOrChangedOnAfter(LocalDateTime after);
}
