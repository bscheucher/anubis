package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Label;

import java.util.List;
import java.util.Optional;

public interface LabelService extends BaseService<Label> {

    List<Label> findAllByLanguage(String language);

    Optional<Label> findByLabelKey(String key);
}
