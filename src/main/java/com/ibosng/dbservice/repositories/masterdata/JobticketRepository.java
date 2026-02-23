package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Jobticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobticketRepository extends JpaRepository<Jobticket, Integer> {
    List<Jobticket> findByStatus(Status status);
    Optional<Jobticket> findByName(String name);
}
