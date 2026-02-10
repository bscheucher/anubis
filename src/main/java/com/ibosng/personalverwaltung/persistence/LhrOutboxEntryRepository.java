package com.ibosng.personalverwaltung.persistence;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LhrOutboxEntryRepository extends JpaRepository<LhrOutboxEntry, Integer> {

    List<LhrOutboxEntry> findByStatusOrderById(LhrOutboxEntry.Status status);

    @Transactional
    @Modifying
    @Query("""
                update LhrOutboxEntry e
                set e.status = 'DONE',
                    e.syncedAt = current_timestamp
                where e.id = :id
            """)
    void markAsProcessed(@NotNull @Param("id") Integer id);

    @Transactional
    @Modifying
    @Query("""
                update LhrOutboxEntry e
                set e.status = 'ERROR',
                    e.syncedAt = current_timestamp,
                    e.errorMessage = :errorMessage
                where e.id = :id
            """)
    void markAsError(@NotNull @Param("id") Integer id, @NotNull @Param("errorMessage") String errorMessage);
}
