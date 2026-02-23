package com.ibosng.personalverwaltung.domain;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class AbwesenheitMapper {
    @Mapping(target = "von", source = "abwesenheitCreateV2.startDate")
    @Mapping(target = "bis", source = "abwesenheitCreateV2.endDate")
    @Mapping(target = "kommentar", source = "abwesenheitCreateV2.comment")
    @Mapping(target = "personalnummer", source = "abwesenheitCreateV2.personalnummer")
    @Mapping(target = "typ", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "beschreibung", ignore = true)
    @Mapping(target = "art", ignore = true)
    @Mapping(target = "idLhr", ignore = true)
    @Mapping(target = "fuehrungskraefte", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "grund", constant = "URLAU")
    @Mapping(target = "commentFuehrungskraft", ignore = true)
    @Mapping(target = "lhrHttpStatus", ignore = true)
    @Mapping(target = "createdBy", constant = GlobalUserHolder.IBOSNG_BACKEND)
    @Mapping(target = "changedBy", constant = GlobalUserHolder.IBOSNG_BACKEND)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "changedOn", ignore = true)
    @Mapping(target = "tage", ignore = true)
    @Mapping(target = "saldo", ignore = true)
    @Mapping(target = "verbaucht", ignore = true)
    public abstract Abwesenheit map(AbwesenheitCreateV2 abwesenheitCreateV2, Benutzer fuehrungskraft);

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

    @AfterMapping
    protected void mapFuehrungskraefte(@MappingTarget Abwesenheit abwesenheit, Benutzer fuehrungskraft) {
        abwesenheit.setFuehrungskraefte(Set.of(fuehrungskraft));
    }
}