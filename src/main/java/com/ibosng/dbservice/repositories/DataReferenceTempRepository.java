package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.DataReferenceTemp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface DataReferenceTempRepository extends JpaRepository<DataReferenceTemp, Integer>, JpaSpecificationExecutor<DataReferenceTemp> {

    List<DataReferenceTemp> findAllByReference(String reference);

}