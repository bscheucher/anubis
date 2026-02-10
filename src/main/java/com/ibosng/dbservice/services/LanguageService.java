package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Language;

import java.util.Optional;

public interface LanguageService extends BaseService<Language> {

    Optional<Language> findByName(String name);
}
