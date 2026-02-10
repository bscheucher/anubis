package com.ibosng.personalverwaltung.domain;

import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.personalverwaltung.domain.exceptions.AbwesenheitCreationException;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntry;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntryRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AbwesenheitenV2Service {

    private final AbwesenheitRespository abwesenheitRepository;
    private final LhrOutboxEntryRepository lhrOutboxEntryRepository;
    private final AbwesenheitMapper mapper;

    @Transactional("postgresTransactionManager")
    public AbwesenheitV2 createAbwesenheit(@NotNull AbwesenheitCreateV2 req) {
        try {
            var abwesenheitPersisted = abwesenheitRepository.save(mapper.map(req));
            var outboxEntryToPersist = LhrOutboxEntry.forCreateAbwesenheitRequest(abwesenheitPersisted.getId());
            lhrOutboxEntryRepository.save(outboxEntryToPersist);

            return mapper.map(abwesenheitPersisted);

        } catch (Exception e) {
            throw new AbwesenheitCreationException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
