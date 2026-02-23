package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;
import com.ibosng.dbservice.repositories.ZeitausgleichRepository;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.urlaub.UrlaubsdatenService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ZeitausgleichServiceImplTest {

    @Mock
    private ZeitausgleichRepository zeitausgleichRepository;
    @Mock
    private PersonalnummerService personalnummerService;
    @Mock
    private BenutzerService benutzerService;
    @Mock
    private UrlaubsdatenService urlaubsdatenService;
    @Mock
    private StammdatenService stammdatenService;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ZeitausgleichServiceImpl zeitausgleichService;

    @Test
    void mapListZeitausgleichToListAbwesenheitDto_shouldMapAndMergeCorrectly() {
        // GIVEN
        Personalnummer pn1 = new Personalnummer();
        pn1.setId(1);
        pn1.setPersonalnummer("PN1");

        Stammdaten stammdaten = new Stammdaten();
        stammdaten.setVorname("John");
        stammdaten.setNachname("Doe");

        Urlaubsdaten urlaubsdaten = new Urlaubsdaten();
        urlaubsdaten.setAnspruch(25.0);

        Benutzer manager = new Benutzer();
        manager.setEmail("manager@example.com");

        when(stammdatenService.findByPersonalnummerId(1)).thenReturn(stammdaten);
        when(urlaubsdatenService.findUrlaubsdatenByPersonalnummerMonth(eq(1), any(LocalDate.class)))
                .thenReturn(Optional.of(urlaubsdaten));

        LocalDate date1 = LocalDate.of(2026, 1, 1);
        LocalDate date2 = LocalDate.of(2026, 1, 2);
        LocalDate date3 = LocalDate.of(2026, 1, 4); // Gap between date2 and date3
        LocalDateTime createdOn = LocalDateTime.of(2026, 1, 1, 10, 0);

        Zeitausgleich z1 = Zeitausgleich.builder()
                .id(1)
                .personalnummer(pn1)
                .datum(date1)
                .status(AbwesenheitStatus.VALID)
                .comment("Work")
                .createdOn(createdOn)
                .fuehrungskraefte(Set.of(manager))
                .commentFuehrungskraft("Approved by manager")
                .build();

        Zeitausgleich z2 = Zeitausgleich.builder()
                .id(2)
                .personalnummer(pn1)
                .datum(date2)
                .status(AbwesenheitStatus.VALID)
                .comment("Work")
                .createdOn(createdOn)
                .fuehrungskraefte(Set.of(manager))
                .commentFuehrungskraft("Approved by manager")
                .build();

        Zeitausgleich z3 = Zeitausgleich.builder()
                .id(3)
                .personalnummer(pn1)
                .datum(date3)
                .status(AbwesenheitStatus.VALID)
                .comment("Work")
                .createdOn(createdOn)
                .fuehrungskraefte(Set.of(manager))
                .commentFuehrungskraft("Approved by manager")
                .build();

        List<Zeitausgleich> zeitausgleichList = List.of(z1, z2, z3);

        // WHEN
        List<AbwesenheitDto> result = zeitausgleichService.mapListZeitausgleichToListAbwesenheitDto(zeitausgleichList);

        // THEN
        // z1 and z2 should be merged because they are adjacent (2026-01-01 and 2026-01-02)
        // z3 should NOT be merged because it is NOT adjacent to z2 (2026-01-04)
        assertEquals(2, result.size());

        // First merged entry (z1 + z2)
        AbwesenheitDto merged1 = result.get(0);
        assertEquals(2, merged1.getId()); // In current implementation, ID of last merged element is set
        assertEquals(1, merged1.getPersonalnummerId());
        assertEquals(date1, merged1.getStartDate());
        assertEquals(date2, merged1.getEndDate());
        assertEquals("2", merged1.getDurationInDays());
        assertEquals("John Doe", merged1.getFullName());
        assertEquals(AbwesenheitType.ZEITAUSGLEICH, merged1.getType());
        assertEquals(AbwesenheitStatus.VALID, merged1.getStatus());
        assertEquals("Work", merged1.getComment());
        assertEquals(25.0, merged1.getAnspruch());
        assertTrue(merged1.isLhrCalculated());
        assertEquals(LocalDate.from(createdOn), merged1.getChangedOn());
        assertEquals("Approved by manager", merged1.getCommentFuehrungskraft());
        assertTrue(merged1.getFuehrungskraefte().contains("manager@example.com"));

        // Second entry (z3)
        AbwesenheitDto entry2 = result.get(1);
        assertEquals(3, entry2.getId());
        assertEquals(1, entry2.getPersonalnummerId());
        assertEquals(date3, entry2.getStartDate());
        assertEquals(date3, entry2.getEndDate());
        assertEquals("1", entry2.getDurationInDays());
        assertEquals("John Doe", entry2.getFullName());
        assertEquals(AbwesenheitType.ZEITAUSGLEICH, entry2.getType());
        assertEquals(AbwesenheitStatus.VALID, entry2.getStatus());
        assertEquals("Work", entry2.getComment());
        assertEquals(25.0, entry2.getAnspruch());
        assertTrue(entry2.isLhrCalculated());
        assertEquals(LocalDate.from(createdOn), entry2.getChangedOn());
        assertEquals("Approved by manager", entry2.getCommentFuehrungskraft());
        assertTrue(entry2.getFuehrungskraefte().contains("manager@example.com"));
    }
}
