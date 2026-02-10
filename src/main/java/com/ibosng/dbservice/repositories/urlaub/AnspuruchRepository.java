package com.ibosng.dbservice.repositories.urlaub;

import com.ibosng.dbservice.entities.urlaub.Anspruch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnspuruchRepository extends JpaRepository<Anspruch, Integer> {
    Anspruch findByLhrId(Integer lhrId);

    Anspruch findByBezeichnung(String bezeichnung);
}
