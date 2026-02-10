package com.ibosng.dbservice.repositories.masterdata;

import com.ibosng.dbservice.entities.masterdata.Kommunalsteuergemeinde;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("postgresTransactionManager")
public interface KommunalsteuergemeindeRepository extends JpaRepository<Kommunalsteuergemeinde, Integer> {
    Kommunalsteuergemeinde findByDienstortPlz(Integer dienstortPlz);
}
