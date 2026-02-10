package com.ibosng.lhrservice.dtos.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.DienstnehmerRefDto;
import lombok.Data;

@Data
@JsonInclude()
public class ZeitdatenStatusDto {
    private DienstnehmerRefDto dienstnehmer;
    private ZeitdatenStatusStatusDto status;
}
