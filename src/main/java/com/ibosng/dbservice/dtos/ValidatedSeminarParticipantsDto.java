package com.ibosng.dbservice.dtos;

import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import lombok.Data;

import java.util.List;

@Data
public class ValidatedSeminarParticipantsDto {

    //TODO Update according to latest JSON Spec from Andreas

    String seminarName;

    List<Teilnehmer> teilnehmerList;
}
