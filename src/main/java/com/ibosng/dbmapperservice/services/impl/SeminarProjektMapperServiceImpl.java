package com.ibosng.dbmapperservice.services.impl;

import com.ibosng.dbibosservice.entities.KeytableIbos;
import com.ibosng.dbibosservice.entities.ProjektIbos;
import com.ibosng.dbibosservice.entities.ProjektPk;
import com.ibosng.dbibosservice.entities.ProjektTypenIbos;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.entities.StandortIbos;
import com.ibosng.dbibosservice.entities.smad.SmAd;
import com.ibosng.dbibosservice.services.KeytableIbosService;
import com.ibosng.dbibosservice.services.ProjektIbosService;
import com.ibosng.dbibosservice.services.ProjektTypenIbosService;
import com.ibosng.dbibosservice.services.StandortIbosService;
import com.ibosng.dbmapperservice.services.SeminarProjektMapperService;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.Projekt2Manager;
import com.ibosng.dbservice.entities.ProjektType;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.seminar.SeminarType;
import com.ibosng.dbservice.services.ProjektService;
import com.ibosng.dbservice.services.ProjektTypeService;
import com.ibosng.dbservice.services.SeminarTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import static com.ibosng.dbmapperservice.utils.Constants.BW_SYNC_SERVICE;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
public class SeminarProjektMapperServiceImpl implements SeminarProjektMapperService {

    private final ProjektIbosService projektIbosService;
    private final ProjektService projektService;
    private final ProjektTypenIbosService projektTypenIbosService;
    private final ProjektTypeService projektTypeService;
    private final SeminarTypeService seminarTypeService;
    private final KeytableIbosService keytableIbosService;
    private final StandortIbosService standortIbosService;

    public SeminarProjektMapperServiceImpl(ProjektIbosService projektIbosService,
                                           ProjektService projektService,
                                           ProjektTypenIbosService projektTypenIbosService,
                                           ProjektTypeService projektTypeService,
                                           SeminarTypeService seminarTypeService,
                                           KeytableIbosService keytableIbosService,
                                           StandortIbosService standortIbosService) {
        this.projektIbosService = projektIbosService;
        this.projektService = projektService;
        this.projektTypenIbosService = projektTypenIbosService;
        this.projektTypeService = projektTypeService;
        this.seminarTypeService = seminarTypeService;
        this.keytableIbosService = keytableIbosService;
        this.standortIbosService = standortIbosService;
    }

    @Override
    public Seminar mapSeminarIbosToSeminar(SeminarIbos seminarIbos) {
        Seminar seminar = new Seminar();
        seminar.setSeminarNummer(seminarIbos.getId());
        if (seminarIbos.getPjnr() != null) {
            Optional<ProjektIbos> projektIbos = projektIbosService.findById(seminarIbos.getPjnr());
            if (projektIbos.isPresent()) {
                Projekt projekt = mapProjektIbosToProjekt(projektIbos.get());
                seminar.setProject(projekt);
            }
        }
        if (!isNullOrBlank(seminarIbos.getBezeichnung1())) {
            seminar.setBezeichnung(seminarIbos.getBezeichnung1());
            seminar.setIdentifier(seminarIbos.getBezeichnung1());
        }
        if (seminarIbos.getZeitVon() != null) {
            seminar.setStartTime(seminarIbos.getZeitVon());
        }
        if (seminarIbos.getZeitBis() != null) {
            seminar.setEndTime(seminarIbos.getZeitBis());
        }
        if (seminarIbos.getDatumVon() != null) {
            seminar.setStartDate(seminarIbos.getDatumVon());
        }
        if (seminarIbos.getDatumBis() != null) {
            seminar.setEndDate(seminarIbos.getDatumBis());
        }

        if (seminarIbos.getMassnahmenNr() != null) {
            seminar.setMassnahmenNr(seminarIbos.getMassnahmenNr());
        }
        if (seminarIbos.getSoStandortId() != null) {
            Optional<StandortIbos> standortIbos = standortIbosService.findById(seminarIbos.getSoStandortId());
            standortIbos.ifPresent(ibos -> seminar.setStandort(ibos.getSoBezeichnung()));
        }

        if (seminarIbos.getZeitVon() != null && seminarIbos.getZeitBis() != null) {
            String uhrzeitString = seminarIbos.getZeitVon().toString() + "-" + seminarIbos.getZeitBis().toString();
            seminar.setSchieneUhrzeit(uhrzeitString);
        }

        if (seminar.getEndDate().isAfter(LocalDate.now())) {
            seminar.setStatus(Status.ACTIVE);
        } else {
            seminar.setStatus(Status.INACTIVE);
        }
        seminar.setCreatedBy(BW_SYNC_SERVICE);
        //TODO FROM FABIO TO OGNEN CHECK IF THIS MAPPING FOR SEMINARTYPE IS FINE LIKE THIS
        if (seminarIbos.getType() != null) {
            Optional<KeytableIbos> keytableIbos = keytableIbosService.findByKyNr(seminarIbos.getType());
            if (keytableIbos.isPresent()) {
                SeminarType seminarType = seminarTypeService.findByName(keytableIbos.get().getKyValueT1());
                if (seminarType == null) {
                    seminarType = new SeminarType();
                    seminarType.setName(keytableIbos.get().getKyValueT1());
                    seminarType.setCreatedBy(BW_SYNC_SERVICE);
                    seminarType = seminarTypeService.save(seminarType);
                }
                seminar.setSeminarType(seminarType);
            }
        }
        return seminar;
    }

    @Override
    public Projekt mapProjektIbosToProjekt(ProjektIbos projektIbos) {
        Projekt projekt = projektService.findByProjektNummer(projektIbos.getId());
        if (projekt == null) {
            projekt = new Projekt();
            if (projektIbos.getId() != null) {
                projekt.setProjektNummer(projektIbos.getId());
            }
            if (projektIbos.getAsNr() != null) {
                projekt.setAuftragNummer(projektIbos.getAsNr());
            }
            if (!isNullOrBlank(projektIbos.getPjBezeichnung1())) {
                projekt.setBezeichnung(projektIbos.getPjBezeichnung1());
            }
            if (projektIbos.getPjDatumVon() != null) {
                projekt.setStartDate(projektIbos.getPjDatumVon());
            }
            if (projektIbos.getPjDatumBis() != null) {
                projekt.setEndDate(projektIbos.getPjDatumBis());
            }
            if (projektIbos.getPjKstGr() != null) {
                projekt.setKostenstelleGruppe(projektIbos.getPjKstGr());
            }
            if (projektIbos.getPjKstNr() != null) {
                projekt.setKostenstelle(projektIbos.getPjKstNr());
            }
            if (projektIbos.getPjKostentraeger() != null) {
                projekt.setKostentraeger(projektIbos.getPjKostentraeger());
            }
            if (projekt.getKostenstelleGruppe() != null && projekt.getKostenstelle() != null && projekt.getKostentraeger() != null) {
                String formattedKostenstelleGruppe = String.format("%02d", projekt.getKostenstelleGruppe());
                String formattedKostenstelle = String.format("%02d", projekt.getKostenstelle());
                String formattedKostentraeger = String.format("%03d", projekt.getKostentraeger());
                String kostentraeger = formattedKostenstelleGruppe + formattedKostenstelle + formattedKostentraeger;

                projekt.setKostentraegerDisplayName(kostentraeger);
            }
            if (projektIbos.getPjErda() != null) {
                projekt.setCreatedOn(projektIbos.getPjErda());
            }
            if (!isNullOrBlank(projektIbos.getPjEruser())) {
                projekt.setCreatedBy(projektIbos.getPjEruser());
            }
            if (projektIbos.getPjAeda() != null) {
                projekt.setChangedOn(projektIbos.getPjAeda());
            }
            if (!isNullOrBlank(projektIbos.getPjAeuser())) {
                projekt.setChangedBy(projektIbos.getPjAeuser());
            }
            if (projektIbos.getPjTyp() != null) {
                Optional<ProjektTypenIbos> projektTypenIbos = projektTypenIbosService.findById(projektIbos.getPjTyp());
                if (projektTypenIbos.isPresent()) {
                    ProjektType projektType = projektTypeService.findByName(projektTypenIbos.get().getBezeichnung());
                    if (projektType == null) {
                        projektType = new ProjektType();
                        projektType.setName(projektTypenIbos.get().getBezeichnung());
                        projektType.setStatus(Status.ACTIVE);
                        projektType.setCreatedBy(BW_SYNC_SERVICE);
                        projektType = projektTypeService.save(projektType);
                    }
                    projekt.setProjektType(projektType);
                }
            }

            projekt = projektService.save(projekt);
        }
        return projekt;
    }

    @Override
    public Seminar2Trainer mapSMADtoSeminar2Trainer(SmAd smadIbos, Seminar seminar, Benutzer trainer) {
        Seminar2Trainer seminar2Trainer = new Seminar2Trainer();
        // Map fields from SmAd to Seminar2Trainer
        seminar2Trainer.setSeminar(seminar);
        seminar2Trainer.setTrainer(trainer);
        seminar2Trainer.setDienstvertragNr(smadIbos.getDienstvertragDvnr());
        seminar2Trainer.setTrainerType(smadIbos.getSmadtrainertyp());
        seminar2Trainer.setStartDate(smadIbos.getSmaddatumvon());
        seminar2Trainer.setEndDate(smadIbos.getSmaddatumbis());
        seminar2Trainer.setRole(smadIbos.getSmadtaetigkeit());
        seminar2Trainer.setStatus(smadIbos.getSmadstatus());
        seminar2Trainer.setCreatedOn(smadIbos.getSmaderda());
        if (smadIbos.getSmadfunktion() != null) {
            Optional<KeytableIbos> keytableIbos = keytableIbosService.findByKyNr(smadIbos.getSmadfunktion());
            keytableIbos.ifPresent(ibos -> seminar2Trainer.setTrainerFunktion(ibos.getKyValueT1()));
        }
        return seminar2Trainer;
    }

    @Override
    public Projekt2Manager mapProjektPk(ProjektPk projektPk, Benutzer benutzer, Projekt projekt) {
        Projekt2Manager pj2manager = new Projekt2Manager();
        pj2manager.setStartDate(projektPk.getBeginn());
        pj2manager.setEndDate(projektPk.getEnde());
        pj2manager.setManager(benutzer);
        pj2manager.setProjekt(projekt);
        return pj2manager;
    }
}
