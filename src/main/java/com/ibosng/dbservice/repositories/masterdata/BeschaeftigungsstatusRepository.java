package com.ibosng.dbservice.repositories.masterdata;


import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsstatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface BeschaeftigungsstatusRepository extends JpaRepository<Beschaeftigungsstatus, Integer> {

    Beschaeftigungsstatus findByName(String name);
}
