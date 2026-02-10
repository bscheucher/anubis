package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.validations.ValidationStatus;
import com.ibosng.dbservice.entities.validations.ValidationType;
import com.ibosng.dbservice.entities.validations.Validations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface ValidationsRepository extends JpaRepository<Validations, Integer> {

    List<Validations> findAllByTypeAndStatus(ValidationType type, ValidationStatus status);

    List<Validations> findAllByType(ValidationType type);

    List<Validations> findAllByStatus(ValidationStatus status);

    List<Validations> findAllByIdentifier(String identifier);
}