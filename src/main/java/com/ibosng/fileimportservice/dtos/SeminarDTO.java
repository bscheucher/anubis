package com.ibosng.fileimportservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeminarDTO {

    @JsonSetter("seminarDate")
    private String seminarDate;

    @JsonSetter("seminarTime")
    private String seminarTime;

    @JsonSetter("level")
    private String level;

    @JsonSetter("seminarCode")
    private String seminarCode;

    @JsonSetter("trainer")
    private String trainer;

    @JsonSetter("kursende")
    private String kursende;

    @JsonSetter("seminarParticipantDtoList")
    private List<ParticipantVHSDto> seminarParticipantDtoList = new ArrayList<>();
}
