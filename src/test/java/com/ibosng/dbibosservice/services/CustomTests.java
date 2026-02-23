package com.ibosng.dbibosservice.services;

import com.ibosng.BaseIntegrationTest;
import com.ibosng.dbibosservice.dtos.*;
import com.ibosng.dbibosservice.dtos.revenue.ProjectForecastDataDto;
import com.ibosng.dbibosservice.dtos.revenue.ProjectRevenueDataDto;
import com.ibosng.dbibosservice.entities.*;
import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragIbos;
import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFixIbos;
import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFixKatCheckedIbos;
import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzIbos;
import com.ibosng.dbibosservice.entities.teilnahme.Teilnahme;
import com.ibosng.dbibosservice.services.impl.AdresseIbosServiceImpl;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragIbosService;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragZusatzFixIbosService;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragZusatzFixKatCheckedIbosService;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragZusatzIbosService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbibosservice.utils.Parsers.parseDate;

@Disabled
@SpringBootTest
@Transactional("mariaDbTransactionManager")
public class CustomTests extends BaseIntegrationTest  {
    @Resource
    private BenutzerIbosService benutzerService;

    @Resource
    private AdresseIbosServiceImpl adresseIbosService;

    @Resource
    private IbisFirmaIbosService ibisFirmaIbosService;

    @Resource
    private MutterspracheIbosService mutterspracheIbosService;

    @Resource
    private ArbeitsvertragIbosService arbeitsvertragIbosService;
    @Resource
    private ArbeitsvertragZusatzIbosService arbeitsvertragZusatzIbosService;

    @Resource
    private ArbeitsvertragZusatzFixIbosService arbeitsvertragZusatzFixIbosService;

    @Resource
    private ArbeitsvertragZusatzFixKatCheckedIbosService arbeitsvertragZusatzFixKatCheckedIbosService;

    @Resource
    private FKostenstelleIbosService fKostenstelleIbosService;
    @Resource
    private KvVerwendungsgruppeIbosService kvVerwendungsgruppeIbosService;

    @Resource
    private KollektivvertragIbosService kollektivvertragIbosService;
    @Resource
    private KvStufeIbosService kvStufeIbosService;

    @Resource
    private KeytableIbosService keytableIbosService;

    @Resource
    private ProjektTypenIbosService projektTypenIbosService;

    @Resource
    private TeilnahmeService teilnahmeService;

    @Resource
    private GrAdIbosService grAdIbosService;

    @Test
    public void grAdIbosServiceTest() {
        List<AdresseIbos> test = adresseIbosService.findAllByChangedAfterWithSeminarData("DB_FW_SYNC_SERVICE",LocalDateTime.of(2025, 7, 28, 0, 0), "tn");
        System.out.println("");
    }

    //43224, 43223, 43219, 43222
    @Test
    public void zeitErfassung() {
        TeilnahmeBasicDto teilnahmesSummary = teilnahmeService.getTeilnehmerSeminarSummary(
                List.of(37454, 37460), LocalDate.of(2024, 10, 1), LocalDate.of(2024, 11, 01));

        List<Teilnahme> teilnahmes = teilnahmeService.findBySeminarSmnrAndInPeriod(
                43222, LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 30));
        System.out.println("");
    }

    @Test
    public void tetad() {
        List<BasicPersonDto> basicPersonDtosFuehrungskraft = benutzerService.getAllFuehrungskraftForMitarbeiter();
        List<BasicPersonDto> basicPersonDtosStartcoach = benutzerService.getAllStartcoachForMitarbeiter();
        String emailFuehrungskraft = benutzerService.getEmailForFuehrungskraftForMitarbeiter(basicPersonDtosFuehrungskraft.get(0).getId());
        String emailStartcoach = benutzerService.getEmailForStartcoachForMitarbeiter(basicPersonDtosStartcoach.get(0).getId());
        Optional<ProjektTypenIbos> projektTypenIbos1 = projektTypenIbosService.findById(1);
        ProjektTypenIbos projektTypenIbos2 = projektTypenIbosService.findByBezeichnung("ÜBA Wien");
        List<KeytableIbos> keytableIbosList = keytableIbosService.findAllByKyNameAndKyValueT1("P_PBfunktion", "RegionalleiterIn");
        KollektivvertragIbos kollektivvertragIbos = kollektivvertragIbosService.findByBezeichnung("BABE");
        List<KvVerwendungsgruppeIbos> kvVerwendungsgruppeIbosList1 = kvVerwendungsgruppeIbosService.findAllByKollektivvertragIdAndBezeichnung(kollektivvertragIbos.getId(), "Verwendungsgruppe 1");
        List<KvVerwendungsgruppeIbos> kvVerwendungsgruppeIbosList2 = kvVerwendungsgruppeIbosService.findAllByKollektivvertragIdAndBezeichnungAndBmdId(kollektivvertragIbos.getId(), "Verwendungsgruppe 1", 1);
        List<KvStufeIbos> kvStufeIbosList = kvStufeIbosService.findAllByBezeichnungAndKvVerwendungsgruppeId("Stufe 1", kvVerwendungsgruppeIbosList1.get(0).getId());
        List<FKostenstelleIbos> fKostenstelleIbosList1 = fKostenstelleIbosService.findAllByIdKstKstGr(92);
        List<FKostenstelleIbos> fKostenstelleIbosList2 = fKostenstelleIbosService.findAllByIdKstKstNr(19);
        List<ArbeitsvertragIbos> arbeitsvertragIbos = arbeitsvertragIbosService.findAllByAddressNo(87273);
        List<ArbeitsvertragZusatzIbos> arbeitsvertragZusatzIbos = arbeitsvertragZusatzIbosService.findAllByArbeitsvertragId(arbeitsvertragIbos.get(0).getId());
        List<ArbeitsvertragZusatzFixIbos> arbeitsvertragZusatzFixIbos = arbeitsvertragZusatzFixIbosService.findAllByArbeitsvertragZusatzId(arbeitsvertragZusatzIbos.get(0).getId());
        List<ArbeitsvertragZusatzFixKatCheckedIbos> arbeitsvertragZusatzFixKatCheckedIbos = arbeitsvertragZusatzFixKatCheckedIbosService.findAllByIdArbeitsvertragZusatzId(arbeitsvertragZusatzIbos.stream().filter(ar -> ar.getId().equals(25153)).findFirst().orElse(null).getId());
        MutterspracheIbos mutterspracheIbos = mutterspracheIbosService.findByName("Bulgarisch");
        IbisFirmaIbos ibisFirmaIbos = ibisFirmaIbosService.findByBmdKlientIdAndLhrKzAndLhrNr(8, "IA", 10);
        System.out.println("");
    }

    @Test
    public void test() {
        List<BasicProjectDto> test1 = benutzerService.findActiveProjectForUser("leopold.fischl");
        List<BasicProjectDto> test2 = benutzerService.findPastProjectForUser("leopold.fischl");
        List<BasicProjectDto> test3 = benutzerService.findFutureProjectForUser("leopold.fischl");
/*        List<BasicProjectDto> test1 = benutzerService.findActiveProjectForUser("kurt.hofer");
        List<BasicProjectDto> test2 = benutzerService.findPastProjectForUser("kurt.hofer");
        List<BasicProjectDto> test3 = benutzerService.findFutureProjectForUser("kurt.hofer");*/

        //ProjectRevenueDataDto projectRevenueDataDto = benutzerService.findRevenueAndHoursFromProjectAndDates(3323, parseDate("2023-11-01"), parseDate("2023-11-30"), false, true);
        //ProjectRevenueDataDto projectRevenueDataDto1 = benutzerService.findRevenueAndHoursFromProjectAndDates(5822, parseDate("2024-01-11"), parseDate("2024-03-31"), false, false);


        ProjectRevenueDataDto projectRevenueDataDto2 = benutzerService.findRevenueAndHoursFromProjectAndDates(5780, parseDate("2023-11-01"), parseDate("2023-11-30"), false, true);
        ProjectRevenueDataDto projectRevenueDataDto3 = benutzerService.findRevenueAndHoursFromProjectAndDates(5780, parseDate("2024-01-17"), parseDate("2024-12-31"), false, false);
        ProjectRevenueDataDto projectRevenueDataDto4 = benutzerService.findRevenueAndHoursFromProjectAndDates(5780, parseDate("2024-01-22"), parseDate("2024-01-28"), false, false);
        ProjectRevenueDataDto projectRevenueDataDto5 = benutzerService.findRevenueAndHoursFromProjectAndDates(5780, parseDate("2024-01-01"), parseDate("2024-01-11"), false, false);
        ProjectForecastDataDto projectForecastDataDto = benutzerService.findProjectForecast(5778);
        System.out.println("");
    }

    @Test
    public void fasf() {
        List<AdresseIbos> testing = adresseIbosService.findAllByChangedAfterAndAderuser("DB_FW_SYNC_SERVICE", LocalDateTime.now().minusDays(1));
        System.out.println("");
    }

    @Test
    public void test1() {
        List<String> dienstorte = arbeitsvertragIbosService.getAllDienstort();
        List<String> kostenstellen = arbeitsvertragIbosService.getAllKonstenstellen();

        List<BasicPersonDto> fuehrungskraft = benutzerService.getAllFuehrungskraftForMitarbeiter();
        List<BasicPersonDto> startcoaches = benutzerService.getAllStartcoachForMitarbeiter();

        List<DienstortDto> dienstort = arbeitsvertragIbosService.findDienstortByBezeichnung("Dornbirn Lustenauerstraße 27");
        System.out.println("");
    }

    @Test
    public void test2() {
        List<ContractDto> test = arbeitsvertragIbosService.getContractsWithoutLeistungen("Thomas.Reiter");
        System.out.println("");
    }
}
