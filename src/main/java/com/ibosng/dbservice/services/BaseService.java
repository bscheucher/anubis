package com.ibosng.dbservice.services;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {

    List<T> findAll();

    Optional<T> findById(Integer id);

    T save(T object);

    List<T> saveAll(List<T> objects);

    void deleteById(Integer id);

    List<T> findAllByIdentifier(String identifier);
}
