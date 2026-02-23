package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.IbisFirmaIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface IbisFirmaIbosRepository extends JpaRepository<IbisFirmaIbos, Integer> {
    IbisFirmaIbos findByBmdKlientIdAndLhrKzAndLhrNr(Integer bmdKlientId, String lhrKz, Integer lhrNr);
}
