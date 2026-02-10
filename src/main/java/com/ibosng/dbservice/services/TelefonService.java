package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.telefon.Telefon;

import java.util.Optional;

public interface TelefonService extends BaseService<Telefon>{
    Optional<Telefon> findByTelefonnummer(Long telefonnummer);
}
