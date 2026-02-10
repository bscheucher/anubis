package com.ibosng.dbservice.dtos.teilnehmer;

import com.ibosng.dbservice.dtos.SeminarDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TeilnehmerSeminarDto {
    private TeilnehmerDto teilnehmerDto;
    private List<SeminarDto> seminarDtos;
    private String pruefungNiveau;
    private String nationPalKz;
    private String ursprungsLandPalKz;
}
