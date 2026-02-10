package com.ibosng.dbmapperservice.services;

import com.ibosng.dbservice.dtos.urlaubsdaten.UrlaubeListDto;
import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;

import java.util.List;

public interface UrlaubsdatenMapper {
    UrlaubeListDto mapToUrlaubeListDto(List<Urlaubsdaten> urlaubsdaten);
}
