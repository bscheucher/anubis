package com.ibosng.personalverwaltung;


import com.ibosng.BaseIntegrationTest;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.DnEintritteDto;
import com.ibosng.personalverwaltung.domain.LhrOutboxScheduler;
import com.ibosng.personalverwaltung.persistence.LhrOutboxEntryRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static com.ibosng.personalverwaltung.persistence.LhrOutboxEntry.Status.DONE;
import static com.ibosng.personalverwaltung.persistence.LhrOutboxEntry.Status.ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SqlGroup({
        @Sql(value = "classpath:db/lhr-sync-integration-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS),
        @Sql(value = "classpath:db/clean-up.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS),
})
public class LhrOutboxIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private LhrOutboxScheduler lhrOutboxScheduler;

    @Autowired
    private LhrOutboxEntryRepository repository;

    @Autowired
    private DataSource dataSource;

    @SpyBean
    private LHRClient lhrClientSpy;

    @Test
    void processCreateAbwesenheitRequest_lhrCalled() {

        // necessary since other tests might already process all existing entries
        resetFirstOutboxEntryToNew();

        lhrOutboxScheduler.process();

        var lhrPayloadCaptor = ArgumentCaptor.forClass(DnEintritteDto.class);
        verify(lhrClientSpy, atLeastOnce())
                .postEintritt(eq("abc"), eq(10), eq(123456789), eq(null), eq(null), lhrPayloadCaptor.capture());

        assertThat(lhrPayloadCaptor.getValue()).satisfies(capturedDto -> {
            assertThat(capturedDto.getDienstnehmer()).satisfies(dienstnehmer -> {
                assertThat(dienstnehmer.getDnNr()).isEqualTo(123456789);
                assertThat(dienstnehmer.getFaKz()).isEqualTo("abc");
                assertThat(dienstnehmer.getFaNr()).isEqualTo(10);
            });
            assertThat(capturedDto.getEintritte())
                    .singleElement()
                    .satisfies(eintritt -> {
                        assertThat(eintritt.getId()).isNull();
                        assertThat(eintritt.getArt()).isNull();
                        assertThat(eintritt.getBeschreibung()).isNull();
                        assertThat(eintritt.getReference()).isNull();
                        assertThat(eintritt.getUnits()).isNull();
                        assertThat(eintritt.getGrund()).isEqualTo("Urlaub");
                        assertThat(eintritt.getKommentar()).isEqualTo("I miss my family");
                        assertThat(eintritt.getSource()).isEqualTo("IbosNG");
                        assertThat(eintritt.getZeitangabe()).satisfies(zeitangabeDto -> {
                            assertThat(zeitangabeDto.getVon()).isEqualTo(LocalDate.now().minusDays(5).format(DateTimeFormatter.ISO_DATE));
                            assertThat(zeitangabeDto.getBis()).isEqualTo(LocalDate.now().plusDays(5).format(DateTimeFormatter.ISO_DATE));
                        });
                    });
        });
    }

    @Test
    void processMultipleRequests_errorsOccur_outboxTableUpdated() {

        lhrOutboxScheduler.process();

        assertThat(repository.findAll())
                .hasSize(4)
                .anySatisfy(e -> {
                    assertThat(e.getId()).isEqualTo(1);
                    assertThat(e.getStatus()).isEqualTo(DONE);
                    assertThat(e.getSyncedAt()).isNotNull();
                    assertThat(e.getErrorMessage()).isNull();
                    assertThat(e.getData()).isEqualTo(Map.of("entityId", "300"));
                })
                .anySatisfy(e -> {
                    assertThat(e.getId()).isEqualTo(2);
                    assertThat(e.getStatus()).isEqualTo(ERROR);
                    assertThat(e.getSyncedAt()).isNotNull();
                    assertThat(e.getErrorMessage()).isNotNull();
                    assertThat(e.getData()).isEqualTo(Map.of("entityId", "302"));
                })
                .anySatisfy(e -> {
                    assertThat(e.getId()).isEqualTo(3);
                    assertThat(e.getStatus()).isEqualTo(DONE);
                    assertThat(e.getSyncedAt()).isNotNull();
                    assertThat(e.getErrorMessage()).isNull();
                    assertThat(e.getData()).isEqualTo(Map.of("entityId", "301"));
                })
                .anySatisfy(e -> {
                    assertThat(e.getId()).isEqualTo(4);
                    assertThat(e.getStatus()).isEqualTo(ERROR);
                    assertThat(e.getSyncedAt()).isNotNull();
                    assertThat(e.getErrorMessage()).isNotNull();
                    assertThat(e.getData()).isEqualTo(Map.of("entityId", "303"));
                });

    }

    @SneakyThrows
    private void resetFirstOutboxEntryToNew() {
        try (var c = dataSource.getConnection();
             var st = c.createStatement()) {
            c.setAutoCommit(false);
            st.execute("UPDATE lhr_outbox SET status = 'NEW' WHERE id = 1");
            c.commit();
        }
    }
}
