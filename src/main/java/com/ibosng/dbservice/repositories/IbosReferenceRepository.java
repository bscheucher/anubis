package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.IbosReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface IbosReferenceRepository extends JpaRepository<IbosReference, Integer> {

    List<IbosReference> findAllByIbosId(Integer ibosId);

    List<IbosReference> findAllByIbosngId(Integer ibosngId);
    List<IbosReference> findAllByIbosIdAndData(Integer ibosId, String data);
    List<IbosReference> findAllByIbosngIdAndData(Integer ibosngId, String data);
    List<IbosReference> findAllByData(String data);
    List<IbosReference> findAllByDataStartingWith(String data);
}
