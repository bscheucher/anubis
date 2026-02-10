package com.ibosng.dbservice.dtos.zeiterfassung;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BasicSeminarDto {
    private Integer id;
    private Integer seminarNumber;
    private String seminarBezeichnung;

    public BasicSeminarDto(Integer id, Integer seminarNumber, String seminarBezeichnung) {
        this.id = id;
        this.seminarNumber = seminarNumber;
        this.seminarBezeichnung = seminarBezeichnung;
    }


}
