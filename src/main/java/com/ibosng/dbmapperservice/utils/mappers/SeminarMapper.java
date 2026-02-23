package com.ibosng.dbmapperservice.utils.mappers;

import com.ibosng.dbibosservice.entities.KeytableIbos;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.services.KeytableIbosService;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.seminar.SeminarType;
import com.ibosng.dbservice.services.SeminarTypeService;

import java.util.Optional;

import static com.ibosng.dbmapperservice.utils.Constants.BW_SYNC_SERVICE;


public class SeminarMapper {

    private final KeytableIbosService keytableIbosService;
    private final SeminarTypeService seminarTypeService;

    public SeminarMapper(SeminarTypeService seminarTypeService, KeytableIbosService keytableIbosService) {
        this.seminarTypeService = seminarTypeService;
        this.keytableIbosService = keytableIbosService;
    }

    public  SeminarIbos seminarToSeminarIbos(Seminar seminar) {
        SeminarIbos seminarIbos = new SeminarIbos();
        seminarIbos.setId(seminar.getSeminarNummer());
        //seminarIbos.setPjnr(seminar.getProject());
        seminarIbos.setBezeichnung1(seminar.getBezeichnung());
        seminarIbos.setZeitVon(seminar.getStartTime());
        seminarIbos.setZeitBis(seminar.getEndTime());
        seminarIbos.setDatumVon(seminar.getStartDate());
        seminarIbos.setDatumBis(seminar.getEndDate());
        return seminarIbos;
    }

    public  Seminar seminarIbosToSeminar(SeminarIbos seminarIbos) {
        Seminar seminar = new Seminar();
        seminar.setSeminarNummer(seminarIbos.getId());
        seminar.setIdentifier(String.valueOf(seminarIbos.getId()));
        //seminar.setProject(seminarIbos.getPjnr());

        if(seminarIbos.getType() != null) {
            Optional<KeytableIbos> keytableIbos = keytableIbosService.findByKyNr(seminarIbos.getType());
            if(keytableIbos.isPresent()) {
                SeminarType seminarType = seminarTypeService.findByName(keytableIbos.get().getKyValueT1());
                if(seminarType == null) {
                    seminarType = new SeminarType();
                    seminarType.setName(keytableIbos.get().getKyValueT1());
                    seminarType.setCreatedBy(BW_SYNC_SERVICE);
                    seminarType = seminarTypeService.save(seminarType);
                }
                seminar.setSeminarType(seminarType);
            }
        }
        seminar.setBezeichnung(seminarIbos.getBezeichnung1() + " " + seminarIbos.getBezeichnung2());
        seminar.setStartTime(seminarIbos.getZeitVon());
        seminar.setEndTime(seminarIbos.getZeitBis());
        seminar.setStartDate(seminarIbos.getDatumVon());
        seminar.setEndDate(seminarIbos.getDatumBis());
        return seminar;
    }
}
