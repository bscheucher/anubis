package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.KVStufe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface KVStufeRepository extends JpaRepository<KVStufe, Integer> {

    @Query("SELECT s FROM KVStufe s WHERE :totalMonths >= s.minMonths AND :totalMonths < s.maxMonths")
    KVStufe findByTotalMonths(int totalMonths);

    List<KVStufe> findAllByName(String name);
}
