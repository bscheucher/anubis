package com.ibosng.gatewayservice.dtos.seminar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor
@AllArgsConstructor
public class SeminarPayload extends SeminarKey{
    private List<Integer> teilnehmer;

    public SeminarPayload(SeminarKey seminarKey, List<Integer> teilnehmer) {
        super(seminarKey.getSeminarNummer(), seminarKey.getName());
        this.teilnehmer = teilnehmer;
    }
}
