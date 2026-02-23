package com.ibosng.teilnehmerportal.mapper;

import com.ibosng.teilnehmerportal.entity.TeilnehmerAbwesenheitsbestaetigung;
import com.ibosng.teilnehmerportal.dto.TeilnehmerAbwesenheitsbestaetigungDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeilnehmerAbwesenheitsbestaetigungMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    TeilnehmerAbwesenheitsbestaetigung toEntity(TeilnehmerAbwesenheitsbestaetigungDto dto);

    TeilnehmerAbwesenheitsbestaetigungDto toDto(TeilnehmerAbwesenheitsbestaetigung entity);
}
