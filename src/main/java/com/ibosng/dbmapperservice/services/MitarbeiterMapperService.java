package com.ibosng.dbmapperservice.services;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.entities.mitarbeiter.KinderIbos;
import com.ibosng.dbibosservice.entities.mitarbeiter.VordienstzeitenIbos;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;

public interface MitarbeiterMapperService {
    StammdatenDto mapIbosDataToStammdatenDto(AdresseIbos adresseIbos);

    VertragsdatenDto mapIbosDataToVertragsdatenDto(AdresseIbos adresseIbos);

    VordienstzeitenDto mapIbosDataToVordienstzeitenDto(VordienstzeitenIbos vordienstzeitenIbos);

    UnterhaltsberechtigteDto mapIbosDataToUnterhaltsberechtigteDto(KinderIbos kinderIbos);
}
