package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional("postgresTransactionManager")
public interface LabelRepository extends JpaRepository<Label, Integer> {

    Optional<Label> findByLabelKey(String key);

    @Query("SELECT l FROM Label l where l.language.name = :language")
    List<Label> findAllByLanguage(String language);
}
