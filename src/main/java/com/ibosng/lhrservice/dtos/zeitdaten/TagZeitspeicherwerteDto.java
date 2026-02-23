package com.ibosng.lhrservice.dtos.zeitdaten;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude()
public class TagZeitspeicherwerteDto {
    private String tag;
    private List<ZeitspeicherwertDto> zeitspeicherWerte;
}
