package com.ibosng.usercreationservice.mapper;

import com.ibosng.dbibosservice.services.BenutzerIbosService;
import com.ibosng.dbservice.entities.*;
import com.ibosng.dbservice.entities.masterdata.Kategorie;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.usercreationservice.dto.UserAnlageDto;
import com.ibosng.usercreationservice.exception.TechnicalException;
import com.ibosng.usercreationservice.service.UserCreationMitarbeiterMapperService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Disabled
public class UserCreationMitarbeiterMapperServiceImplTest {

    private Stammdaten stammdaten;
    private Vertragsdaten vertragsdaten;
    private Adresse adresse;
    private Plz plz;
    private Bundesland bundesland;
    @Mock
    private BenutzerIbosService benutzerIbosService;

    @InjectMocks
    private UserCreationMitarbeiterMapperService userCreationMitarbeiterMapperService;

    @BeforeEach
    void setup() {
        Personalnummer personalnummer = new Personalnummer();
        personalnummer.setPersonalnummer("12345");

        Kostenstelle kostenstelle = new Kostenstelle();
        kostenstelle.setBezeichnung("IT");

        stammdaten = new Stammdaten();
        stammdaten.setPersonalnummer(personalnummer);
        stammdaten.setVorname("John");
        stammdaten.setNachname("Doe");

        bundesland = new Bundesland();
        bundesland.setName("Bavaria");

        plz = new Plz();
        plz.setPlz(80331);
        plz.setBundesland(bundesland);

        adresse = new Adresse();
        adresse.setPlz(plz);
        adresse.setOrt("Munich");
        adresse.setStrasse("Some Street");

        Dienstort dienstort = new Dienstort();
        dienstort.setAdresse(adresse);

        vertragsdaten = new Vertragsdaten();
        vertragsdaten.setDienstort(dienstort);
        Benutzer fuehrungskraft = new Benutzer();
        fuehrungskraft.setFirstName("Manager");
        vertragsdaten.setFuehrungskraft(fuehrungskraft);
        Benutzer startcoach = new Benutzer();
        fuehrungskraft.setFirstName("Coach");
        vertragsdaten.setStartcoach(startcoach);
        vertragsdaten.setEintritt(LocalDate.now());
        vertragsdaten.setKostenstelle(kostenstelle);

        Kategorie kategorie = new Kategorie();
        kategorie.setName("Category");

        vertragsdaten.setKategorie(kategorie);
        vertragsdaten.setBefristungBis(LocalDate.now().plusDays(365));
        when(benutzerIbosService.getEmailForFuehrungskraftForMitarbeiter(anyLong())).thenReturn("Manager");
        when(benutzerIbosService.getEmailForStartcoachForMitarbeiter(anyLong())).thenReturn("Coach");
    }

    @Test
    void testToDtoSuccess() throws TechnicalException {
        UserAnlageDto dto = userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten);

        assertThat(dto).isNotNull();
        assertThat(dto.getPersonalnummer()).isEqualTo("12345");
        assertThat(dto.getVorname()).isEqualTo("John");
        assertThat(dto.getNachname()).isEqualTo("Doe");
        assertThat(dto.getKostenstelle()).isEqualTo("IT");
        assertThat(dto.getDienstort().getBundesland()).isEqualTo("Bavaria");
        assertThat(dto.getDienstort().getPlz()).isEqualTo("80331");
        assertThat(dto.getDienstort().getStadt()).isEqualTo("Munich");
        assertThat(dto.getDienstort().getAnschrift()).isEqualTo("Some Street");
        assertThat(dto.getFuehrungskraft()).isEqualTo("Manager");
        assertThat(dto.getStartcoach()).isEqualTo("Coach");
        assertThat(dto.getEintrittAm()).isEqualTo(LocalDate.now());
        assertThat(dto.getKategorie()).isEqualTo("Category");
        assertThat(dto.getBefristungBis()).isEqualTo(LocalDate.now().plusDays(365));
    }

    @Test
    void testToDtoStammdatenNullThrowsException() {
        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(null, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Stammdaten or vertragsdaten are null");
    }

    @Test
    void testToDtoVertragsdatenNullThrowsException() {
        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, null))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Stammdaten or vertragsdaten are null");
    }

    @Test
    void testToDtoPersonalnummerNullThrowsException() {
        stammdaten.setPersonalnummer(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Personalnummer is null or blank");
    }

    @Test
    void testToDtoVornameNullThrowsException() {
        stammdaten.setVorname(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Vorname is null or blank");
    }

    @Test
    void testToDtoNachnameNullThrowsException() {
        stammdaten.setNachname(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Nachname is null or blank");
    }

    @Test
    void testToDtoBundeslandNullThrowsException() {
        bundesland.setName(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Bundesland is null or blank");
    }

    @Test
    void testToDtoPlzNullThrowsException() {
        plz.setPlz(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("PLZ is null or blank");
    }

    @Test
    void testToDtoOrtNullThrowsException() {
        adresse.setOrt(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Ort is null or blank");
    }

    @Test
    void testToDtoStrasseNullThrowsException() {
        adresse.setStrasse(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Strasse is null or blank");
    }

    @Test
    void testToDtoDienstortNullThrowsException() {
        vertragsdaten.setDienstort(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Dienstort is null");
    }

    @Test
    void testToDtoEintrittNullThrowsException() {
        vertragsdaten.setEintritt(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Eintritt is null");
    }

    @Test
    void testToDtoKategorieNullThrowsException() {
        vertragsdaten.setKategorie(null);

        assertThatThrownBy(() -> userCreationMitarbeiterMapperService.toDto(stammdaten, vertragsdaten))
                .isInstanceOf(TechnicalException.class)
                .hasMessage("Kategorie is null or blank");
    }
}
