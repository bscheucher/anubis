package com.ibosng.dbservice.repositories.history;

import com.ibosng.dbservice.entities.history.TeilnehmerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerHistoryRepository extends JpaRepository<TeilnehmerHistory, Integer> {

    List<TeilnehmerHistory> findAllByActionAndActionTimestampAfter(Character action, LocalDateTime actionTimestamp);
}
