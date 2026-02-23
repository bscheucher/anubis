package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.repositories.AdresseIbosRepository;
import com.ibosng.dbibosservice.services.AdresseIbosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdresseIbosServiceImpl implements AdresseIbosService {
    private final AdresseIbosRepository adresseIbosRepository;


    @Override
    public List<AdresseIbos> findAll() {
        return adresseIbosRepository.findAll();
    }

    @Override
    public Optional<AdresseIbos> findById(Integer id) {
        return adresseIbosRepository.findById(id);
    }

    @Override
    public AdresseIbos save(AdresseIbos object) {
        return adresseIbosRepository.save(object);
    }

    @Override
    public List<AdresseIbos> saveAll(List<AdresseIbos> objects) {
        return adresseIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        adresseIbosRepository.deleteById(id);
    }

    @Override
    public Page<AdresseIbos> findAllChangedAfterPageable(LocalDateTime after, Pageable pageable) {
        return adresseIbosRepository.findAllByAderdaAfterOrAdaedaAfter(after, after, pageable);
    }

    @Override
    public List<AdresseIbos> findAllByAdaedaAfterAndAdtyp(LocalDateTime after, String type) {
        return adresseIbosRepository.findAllByAdaedaAfterAndAdtyp(after, type);
    }

    @Override
    public Page<AdresseIbos> findAllByAderuser(String createdBy, Pageable pageable) {
        return adresseIbosRepository.findAllByAderuser(createdBy, pageable);
    }

    @Override
    public List<AdresseIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after) {
        return adresseIbosRepository.findAllByAderuserAndAderdaAfterOrAderuserAndAdaedaAfter(createdBy, after, createdBy, after);

    }

    @Override
    public List<AdresseIbos> findAllByChangedAfterAndAderuserAndType(String createdBy, LocalDateTime after, String type) {
        return adresseIbosRepository.findAllByAderuserAndAderdaAfterAndAdtypOrAderuserAndAdaedaAfterAndAdtyp(createdBy, after, type, createdBy, after, type);
    }

    @Override
    public List<AdresseIbos> findAllByAdpersnrAndAderuser(String personalnummer, String eruser) {
        return adresseIbosRepository.findAllByAdpersnrAndAderuser(personalnummer, eruser);
    }

    @Override
    public List<AdresseIbos> findAllByVornameAndNachname(String vorname, String nachname) {
        return adresseIbosRepository.findAllByAdvnf2AndAdznf1(vorname, nachname);
    }

    @Override
    public List<AdresseIbos> findAllByAdemail1(String email) {
        return adresseIbosRepository.findAllByAdemail1(email);
    }

    @Override
    public List<AdresseIbos> findAllByAdemail2(String email) {
        return adresseIbosRepository.findAllByAdemail2(email);
    }

    @Override
    public List<AdresseIbos> getFilteredTeilnehmer(String searchTerm, String sortBy, String sortDir) {

        List<Object[]> foundEntries;
        if (sortDir != null && sortDir.equalsIgnoreCase("DESC")) {
            foundEntries = adresseIbosRepository.getFilteredTeilnehmerDesc(searchTerm, sortBy);
        } else {
            foundEntries = adresseIbosRepository.getFilteredTeilnehmerAsc(searchTerm, sortBy);
        }
        return foundEntries.stream()
                .map(this::mapToAdresseIbos)
                .toList();
//        return null;
    }

    @Override
    public List<AdresseIbos> findValidAdresseFromWorkEmail(String workEmail) {
        return adresseIbosRepository.findValidAdresseFromWorkEmail(workEmail);
    }

    @Override
    public List<AdresseIbos> findAdresseIbosFromPersonalnummer(String personalnummer, Integer ibisFirma) {
        return adresseIbosRepository.findAdresseIbosFromPersonalnummer(personalnummer, ibisFirma);
    }

    @Override
    public Integer getAdresseIdFromPersonalnummer(String personalnummer, Integer ibisFirma) {
        List<AdresseIbos> adresseIbosList = findAdresseIbosFromPersonalnummer(personalnummer, ibisFirma);
        if (adresseIbosList.isEmpty()) {
            log.warn("No adresse found for personalnummer {}", personalnummer);
            return null;
        } else if (adresseIbosList.size() > 1) {
            log.warn("Multiple adresses found for personalnummer {}", personalnummer);
        }
        return adresseIbosList.get(0).getAdadnr();
    }

    @Override
    public Optional<AdresseIbos> findByFirstNameAndLastNameAndSvnrAndCreationUser(String firstName, String lastName, String svnr, String createdBy) {
        AdresseIbos adresseIbos = adresseIbosRepository.findOneByAdvnf2AndAdznf1AndAdsvnrAndAderuser(firstName, lastName, svnr, createdBy);
        if (adresseIbos != null) {
            return Optional.of(adresseIbos);
        }
        return Optional.empty();
    }

    @Override
    public List<String> findEmailsOfActiveFuehrungskraefte() {
        return adresseIbosRepository.findEmailsOfActiveFuehrungskraefte();
    }

    @Override
    public List<String> findEmailsOfActiveStartcoaches() {
        return adresseIbosRepository.findEmailsOfActiveStartcoaches();
    }

    @Override
    public List<AdresseIbos> findValidAdresseFromWorkEmailAndVertragFix(String workEmail) {
        return adresseIbosRepository.findValidAdresseFromWorkEmailAndVertragFix(workEmail);
    }

    @Override
    public String getFuehrungskraftFromEmail(String email) {
        return adresseIbosRepository.getFuehrungskraftFromEmail(email);
    }

    @Override
    public String getFuehrungskraftFromLogin(String login) {
        return adresseIbosRepository.getFuehrungskraftFromLogin(login);
    }

    @Override
    public String getFuehrungskraftUPNFromLogin(String login) {
        return adresseIbosRepository.getFuehrungskraftUPNFromLogin(login);
    }

    @Override
    public String getAdresseFromUPN(String upn) {
        return adresseIbosRepository.getAdresseFromUPN(upn);
    }

    @Override
    public List<AdresseIbos> findAllByChangedAfterWithSeminarData(String createdBy, LocalDateTime after, String type) {
        return adresseIbosRepository.findAllChangedInTheLastTimestamp(createdBy, after, type, createdBy, after, type);
    }

    @Override
    public String findKostenstelle2UpnByKostenstelleId(int id) {
        return adresseIbosRepository.findKostenstelle2UpnByKostenstelleId(id);
    }

    private AdresseIbos mapToAdresseIbos(Object[] row) {
        AdresseIbos adresseIbos = new AdresseIbos();


        adresseIbos.setAdadnr(row[0] != null ? ((Long) row[0]).intValue() : null);
        adresseIbos.setAdtyp(String.valueOf(row[1]));
        adresseIbos.setAdfadnr(row[2] != null ? ((Long) row[2]).intValue() : null);
        adresseIbos.setAdibisGs(row[3] != null ? ((Long) row[3]).intValue() : null);
        adresseIbos.setAdibisGseinsatz(String.valueOf(row[4]));
        adresseIbos.setAdadrtype((String.valueOf(row[5])));
        adresseIbos.setAdanrede(row[6] != null ? ((Long) row[6]).intValue() : null);
        adresseIbos.setAdgeschlecht((String.valueOf(row[7])));
        adresseIbos.setAdtitelOld(row[8] != null ? ((Long) row[8]).intValue() : null);
        adresseIbos.setAdtitel(row[9] != null ? ((Long) row[9]).intValue() : null);
        adresseIbos.setAdtitelv(row[10] != null ? ((Long) row[10]).intValue() : null);
        adresseIbos.setAdznf1(String.valueOf(row[11]));
        adresseIbos.setAdvnf2(String.valueOf(row[12]));
        adresseIbos.setAdsube(String.valueOf(row[13]));
        adresseIbos.setAdstrasse(String.valueOf(row[14]));
        adresseIbos.setAdstrobjekt(String.valueOf(row[15]));
        adresseIbos.setAdfsb(row[16] != null ? ((Long) row[16]).intValue() : null);
        adresseIbos.setAdfsbKopieUebergeben((Boolean) row[17]);
        adresseIbos.setAdlkz(String.valueOf(row[18]));
        adresseIbos.setAdplz(String.valueOf(row[19]));
        adresseIbos.setAdort(String.valueOf(row[20]));
        adresseIbos.setAdplz2(String.valueOf(row[21]));
        adresseIbos.setAdlkz2(String.valueOf(row[22]));
        adresseIbos.setAdort2(String.valueOf(row[23]));
        adresseIbos.setAdstrasse2(String.valueOf(row[24]));
        adresseIbos.setAdbundesland(row[25] != null ? ((Long) row[25]).intValue() : null);
        adresseIbos.setAdzukz(String.valueOf(row[26]));
        adresseIbos.setAdtelp(String.valueOf(row[27]));
        adresseIbos.setAdfaxp(String.valueOf(row[28]));
        adresseIbos.setAdtelf(String.valueOf(row[29]));
        adresseIbos.setAdfaxf(String.valueOf(row[30]));
        adresseIbos.setAdsipnr(String.valueOf(row[31]));
        adresseIbos.setAdmobil1(String.valueOf(row[32]));
        adresseIbos.setAdmobil1Besitzer(String.valueOf(row[33]));
        adresseIbos.setAdmobil2(String.valueOf(row[34]));
        adresseIbos.setAdmobil2Besitzer(String.valueOf(row[35]));
        adresseIbos.setAdemail1(String.valueOf(row[36]));
        adresseIbos.setAdemail2(String.valueOf(row[37]));
        adresseIbos.setAdinternet(String.valueOf(row[38]));
        adresseIbos.setAdgebdatum(mapLocalDateRow(row[39]));
        adresseIbos.setAdgebdatumf(row[40] != null ? ((Long) row[40]).intValue() : null);
        adresseIbos.setAdgebort(String.valueOf(row[41]));
        adresseIbos.setAdgebland(String.valueOf(row[42]));
        adresseIbos.setAdstaatsb(row[43] != null ? ((Integer) row[43]) : null);
        adresseIbos.setAdstaatenlos(String.valueOf(row[44]));
        adresseIbos.setAdmuttersprache(row[45] != null ? ((Long) row[45]).intValue() : null);
        adresseIbos.setAdmutterspracheKeineAngabe((Boolean) row[46]);
        adresseIbos.setAdfamstand(row[47] != null ? ((Long) row[47]).intValue() : null);
        adresseIbos.setAderstkontaktAm(mapLocalDateRow(row[48]));
        adresseIbos.setAdbewerbungAm(mapLocalDateRow(row[49]));
        adresseIbos.setAdersteintritt(mapLocalDateRow(row[50]));
        adresseIbos.setAdpersnr(String.valueOf(row[51]));
        adresseIbos.setAdsvnr(String.valueOf(row[52]));
        adresseIbos.setAdsvnrUnbekannt(row[53] != null ? ((Long) row[53]).intValue() : null);
        adresseIbos.setAdversicherung(String.valueOf(row[54]));
        adresseIbos.setAdmitversichertbei(String.valueOf(row[55]));
        adresseIbos.setAderzBe(String.valueOf(row[56]));
        adresseIbos.setAderzBeTel(String.valueOf(row[57]));
        adresseIbos.setAderzBeMail(String.valueOf(row[58]));
        adresseIbos.setAdvorM(String.valueOf(row[59]));
        adresseIbos.setAdvorMTel(String.valueOf(row[60]));
        adresseIbos.setAdvorMMail(String.valueOf(row[61]));
        adresseIbos.setAdarbeitsgenehmigung(String.valueOf(row[62]));
        adresseIbos.setAdarbeitsgenbis(mapLocalDateRow(row[63]));
        adresseIbos.setAdfuehrerschein(String.valueOf(row[64]));
        adresseIbos.setAdpruefungen(String.valueOf(row[65]));
        adresseIbos.setAdberuf(String.valueOf(row[66]));
        adresseIbos.setAdfunktion(row[67] != null ? ((Long) row[67]).intValue() : null);
        adresseIbos.setAdkreditornr(String.valueOf(row[68]));
        adresseIbos.setAdbank(String.valueOf(row[69]));
        adresseIbos.setAdbankblz(String.valueOf(row[70]));
        adresseIbos.setAdbankkonto(String.valueOf(row[71]));
        adresseIbos.setAdbankiban(String.valueOf(row[72]));
        adresseIbos.setAdbankbic(String.valueOf(row[73]));
        adresseIbos.setAdfbnr(String.valueOf(row[74]));
        adresseIbos.setAdfbnrgericht(String.valueOf(row[75]));
        adresseIbos.setAdsteuernr(String.valueOf(row[76]));
        adresseIbos.setAdfinanzamt(String.valueOf(row[77]));
        adresseIbos.setAduid(String.valueOf(row[78]));
        adresseIbos.setAdklientid(row[79] != null ? ((Long) row[79]).intValue() : null);
        adresseIbos.setAdklientidOld(row[80] != null ? ((Byte) row[80]).intValue() : null);
        adresseIbos.setAdbemerkung(String.valueOf(row[81]));
        adresseIbos.setAdnoml(String.valueOf(row[82]));
        adresseIbos.setAdfoto(String.valueOf(row[83]));
        adresseIbos.setAdbildLink(String.valueOf(row[84]));
        adresseIbos.setAdkategorieOld(row[85] != null ? ((Long) row[85]).intValue() : null);
        adresseIbos.setAdkategorie(row[86] != null ? ((Long) row[86]).intValue() : null);
        adresseIbos.setAdgewerbeschein(row[87] != null ? ((Long) row[87]).intValue() : null);
        adresseIbos.setAdowner(String.valueOf(row[88]));
        adresseIbos.setAdberecht(String.valueOf(row[89]));
        adresseIbos.setAdquelle(String.valueOf(row[90]));
        adresseIbos.setAdprda(mapLocalDateRow(row[91]));
        adresseIbos.setAdpruser(String.valueOf(row[92]));
        adresseIbos.setAdstatus((Character) row[93]);
        adresseIbos.setAduserid(String.valueOf(row[94]));
        adresseIbos.setAdpwd(String.valueOf(row[95]));
        adresseIbos.setAdtcstatus(String.valueOf(row[96]));
        adresseIbos.setAdsettinglistlg(row[97] != null ? ((Long) row[97]).intValue() : null);
        adresseIbos.setAdlastlogin((LocalDateTime) row[98]);
        adresseIbos.setAdmbbeUebertritt(mapLocalDateRow(row[99]));
        adresseIbos.setAdbBcreate((Character) row[100]);
        adresseIbos.setAdloek(String.valueOf(row[101]));
        adresseIbos.setAdaeda(((Timestamp) row[102]).toLocalDateTime());
        adresseIbos.setAdaeuser((String.valueOf(row[103])));
        adresseIbos.setAderda(((Timestamp) row[104]).toLocalDateTime());
        adresseIbos.setAderuser((String.valueOf(row[105])));

        return adresseIbos;
    }

    private LocalDate mapLocalDateRow(Object row) {
        return row != null ? ((Date) row).toLocalDate() : null;
    }
}
