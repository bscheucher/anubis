package com.ibosng.validationservice.services;

import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;

public interface UnterhaltsberechtigteValidatorService {

    Unterhaltsberechtigte getUnterhaltsberechtige(UnterhaltsberechtigteDto unterhaltsberechtigteDto);
}
