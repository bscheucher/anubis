package com.ibosng.dbmapperservice.services;

import com.ibosng.dbibosservice.entities.ProjektIbos;
import com.ibosng.dbibosservice.entities.ProjektPk;
import com.ibosng.dbibosservice.entities.SeminarIbos;
import com.ibosng.dbibosservice.entities.smad.SmAd;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.Projekt2Manager;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;

public interface SeminarProjektMapperService {
    Seminar mapSeminarIbosToSeminar(SeminarIbos seminarIbos);

    Projekt mapProjektIbosToProjekt(ProjektIbos projektIbos);

    Seminar2Trainer mapSMADtoSeminar2Trainer(SmAd smadIbos, Seminar seminar, Benutzer trainer);

    Projekt2Manager mapProjektPk(ProjektPk projektPk, Benutzer benutzer, Projekt projekt);
}
