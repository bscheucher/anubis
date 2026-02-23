package com.ibosng.personalverwaltung.web;

import com.ibosng.personalverwaltung.domain.AbwesenheitCreateV2;
import com.ibosng.personalverwaltung.domain.AbwesenheitV2;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AbwesenheitV2DtoMapper {

    @Mapping(target = "startDate", source = "dto.startDate")
    @Mapping(target = "endDate", source = "dto.endDate")
    @Mapping(target = "type", source = "dto.type")
    @Mapping(target = "comment", source = "dto.comment")
    @Mapping(target = "personalnummer", source = "personalnummer")
    public abstract AbwesenheitCreateV2 map(AbwesenheitCreateV2Dto dto, Personalnummer personalnummer);

    public abstract AbwesenheitV2Dto map(AbwesenheitV2 abwesenheitV2);
}