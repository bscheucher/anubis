package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.seminar.SeminarPruefungArt;
import com.ibosng.dbservice.services.BaseService;

public interface SeminarPruefungArtService extends BaseService<SeminarPruefungArt> {

    SeminarPruefungArt findByName(String name);
}
