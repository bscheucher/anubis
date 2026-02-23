package com.ibosng.lhrservice.services;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;

public interface HelperService {

    DienstnehmerRefDto createDienstnehmerRefDto(String personalnummer);

    DienstnehmerRefDto createDienstnehmerRefDto(Personalnummer personalnummer);

    Benutzer getFuehrungskraefte(Personalnummer personalnummer);
}
