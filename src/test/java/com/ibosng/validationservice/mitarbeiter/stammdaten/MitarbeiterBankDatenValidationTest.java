package com.ibosng.validationservice.mitarbeiter.stammdaten;

import com.ibosng.BaseIntegrationTest;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.BankDaten;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.dbservice.services.mitarbeiter.BankDatenService;
import com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten.MitarbeiterBankDatenValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Disabled
public class MitarbeiterBankDatenValidationTest extends BaseIntegrationTest {

    private MitarbeiterBankDatenValidation validation;
    private Stammdaten stammdaten;
    private StammdatenDto stammdatenDto;
    private BankDaten bankDaten;
    private Land land;

    @Mock
    private BankDatenService bankDatenService;

    @Mock
    private LandService landService;

    @Mock
    private GlobalUserHolder globalUserHolder;

    @BeforeEach
    void setUp() {
        validation = new MitarbeiterBankDatenValidation(bankDatenService, landService, globalUserHolder);
        stammdatenDto = new StammdatenDto();
        stammdaten = new Stammdaten();
        bankDaten = new BankDaten();
        stammdatenDto.setBankcard(BlobStatus.VERIFIED.getValue());
        Personalnummer personalnummer = new Personalnummer();
        personalnummer.setMitarbeiterType(MitarbeiterType.MITARBEITER);
        stammdaten.setPersonalnummer(personalnummer);
        land = new Land();
        Land landAt = new Land();
        landAt.setLandCode("AT");
        landAt.setTelefonvorwahl("+43");
        when(landService.getLandFromCountryCode("+43")).thenReturn(landAt);
    }


    @Test
    void whenIbanBicATCorrect() {
        stammdatenDto.setBank("UNICREDIT BANK AUSTRIA AG");
        stammdatenDto.setIban("AT021200000703447144");
        stammdatenDto.setBic("BKAUATWW");

        land.setLandCode("AT");
        land.setTelefonvorwahl("+43");
        when(landService.findByLandCode("AT")).thenReturn(List.of(land));

        when(bankDatenService.save(any(BankDaten.class))).thenReturn(bankDaten);
        boolean result = validation.executeValidation(stammdatenDto, stammdaten);

        assertTrue(result);
        assertTrue(stammdaten.getErrors().isEmpty());
    }

    @Test
    void whenIbanBicDECorrect() {
        stammdatenDto.setBank("TARGOBANK");
        stammdatenDto.setIban("DE02300209000106531065");
        stammdatenDto.setBic("CMCIDEDD");

        land.setLandCode("DE");
        land.setTelefonvorwahl("+49");
        when(landService.findByLandCode("DE")).thenReturn(List.of(land));

        when(bankDatenService.save(any(BankDaten.class))).thenReturn(bankDaten);
        boolean result = validation.executeValidation(stammdatenDto, stammdaten);

        assertTrue(result);
        assertTrue(stammdaten.getErrors().isEmpty());
    }

    @Test
    void whenIbanBicSchweizCorrect() {
        stammdatenDto.setBank("ZÜRCHER KANTONALBANK");
        stammdatenDto.setIban("CH0200700110000387896");
        stammdatenDto.setBic("ZKBKCHZZ80A");
        land.setLandCode("CH");
        land.setTelefonvorwahl("+41");
        when(landService.findByLandCode("CH")).thenReturn(List.of(land));

        when(bankDatenService.save(any(BankDaten.class))).thenReturn(bankDaten);
        boolean result = validation.executeValidation(stammdatenDto, stammdaten);

        assertTrue(result);
        assertTrue(stammdaten.getErrors().isEmpty());
    }

    @Test
    void whenIbanBicLichtensteinCorrect() {
        stammdatenDto.setBank("LIECHTENSTEINISCHE LANDESBANK AG");
        stammdatenDto.setIban("LI0208800000017197386");
        stammdatenDto.setBic("LILALI2X");

        land.setLandCode("LI");
        land.setTelefonvorwahl("+423");
        when(landService.findByLandCode("LI")).thenReturn(List.of(land));
        when(bankDatenService.save(any(BankDaten.class))).thenReturn(bankDaten);
        boolean result = validation.executeValidation(stammdatenDto, stammdaten);

        assertTrue(result);
        assertTrue(stammdaten.getErrors().isEmpty());
    }


    @Test
    void fromTicket9164CH() {
        stammdatenDto.setBank("POSTFINANCE AG");
        stammdatenDto.setIban("CH0209000000100013997");
        stammdatenDto.setBic("POFICHBE");

        land.setLandCode("CH");
        land.setTelefonvorwahl("+41");
        when(landService.findByLandCode("CH")).thenReturn(List.of(land));
        when(bankDatenService.save(any(BankDaten.class))).thenReturn(bankDaten);
        boolean result = validation.executeValidation(stammdatenDto, stammdaten);

        assertTrue(result);
        assertTrue(stammdaten.getErrors().isEmpty());
    }

    @Test
    void fromTicket9164DE() {
        stammdatenDto.setBank("DEUTSCHE KREDITBANK BERLIN");
        stammdatenDto.setIban("DE02120300000000202051");
        stammdatenDto.setBic("BYLADEM1001");

        land.setLandCode("DE");
        land.setTelefonvorwahl("+49");
        when(landService.findByLandCode("DE")).thenReturn(List.of(land));
        when(bankDatenService.save(any(BankDaten.class))).thenReturn(bankDaten);
        boolean result = validation.executeValidation(stammdatenDto, stammdaten);

        assertTrue(result);
        assertTrue(stammdaten.getErrors().isEmpty());
    }
}
