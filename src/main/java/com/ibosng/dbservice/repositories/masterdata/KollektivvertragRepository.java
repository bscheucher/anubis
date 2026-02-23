package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Kollektivvertrag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface KollektivvertragRepository extends JpaRepository<Kollektivvertrag, Integer> {

    Kollektivvertrag findByName(String name);
}
