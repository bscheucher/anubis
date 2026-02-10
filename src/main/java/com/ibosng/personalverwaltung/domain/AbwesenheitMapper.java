package com.ibosng.personalverwaltung.domain;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AbwesenheitMapper {
    @Mapping(target = "von", source = "startDate")
    @Mapping(target = "bis", source = "endDate")
    @Mapping(target = "kommentar", source = "comment")
    @Mapping(target = "typ", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beschreibung", ignore = true)
    @Mapping(target = "art", ignore = true)
    @Mapping(target = "idLhr", ignore = true)
    @Mapping(target = "fuehrungskraefte", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "grund", ignore = true)
    @Mapping(target = "commentFuehrungskraft", ignore = true)
    @Mapping(target = "lhrHttpStatus", ignore = true)
    @Mapping(target = "createdBy", constant = GlobalUserHolder.IBOSNG_BACKEND)
    @Mapping(target = "changedBy", constant = GlobalUserHolder.IBOSNG_BACKEND)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "changedOn", ignore = true)
    @Mapping(target = "tage", ignore = true)
    @Mapping(target = "saldo", ignore = true)
    @Mapping(target = "verbaucht", ignore = true)
    public abstract Abwesenheit map(AbwesenheitCreateV2 abwesenheitCreateV2);

    @Mapping(target = "startDate", source = "von")
    @Mapping(target = "endDate", source = "bis")
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "comment", source = "kommentar")
    public abstract AbwesenheitV2 map(Abwesenheit abwesenheit);

    @AfterMapping
    protected void mapStatus(@MappingTarget Abwesenheit abwesenheit) {
        abwesenheit.setStatus(AbwesenheitStatus.VALID);
    }

    @AfterMapping
    protected void mapType(@MappingTarget AbwesenheitV2 abwesenheitV2, Abwesenheit abwesenheit) {
        abwesenheitV2.setType(AbwesenheitType.fromValue(abwesenheit.getTyp()));
    }

    @AfterMapping
    protected void mapTyp(@MappingTarget Abwesenheit abwesenheit, AbwesenheitCreateV2 abwesenheitCreateV2) {
        abwesenheit.setTyp(abwesenheitCreateV2.getType().getValue());
    }
}