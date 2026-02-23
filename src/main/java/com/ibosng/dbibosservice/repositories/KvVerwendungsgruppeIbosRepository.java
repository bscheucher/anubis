package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.KvVerwendungsgruppeIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface KvVerwendungsgruppeIbosRepository extends JpaRepository<KvVerwendungsgruppeIbos, Integer> {
    List<KvVerwendungsgruppeIbos> findAllByKollektivvertragIdAndBezeichnung(Integer kollektivvertragId, String bezeichnung);

    List<KvVerwendungsgruppeIbos> findAllByKollektivvertragIdAndBezeichnungAndBmdId(Integer kollektivvertragId, String bezeichnung, Integer bmdId);
}
