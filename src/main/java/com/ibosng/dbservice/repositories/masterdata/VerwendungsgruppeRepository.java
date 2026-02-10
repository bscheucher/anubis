package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface VerwendungsgruppeRepository extends JpaRepository<Verwendungsgruppe, Integer> {
    Verwendungsgruppe findByName(String name);

    @Query(value = "select ver from Verwendungsgruppe ver where ver.kollektivvertrag.name = :kollektivvertrag")
    List<Verwendungsgruppe> findAllByKollektivvertragName(String kollektivvertrag);
}
