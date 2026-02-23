package com.ibosng.dbservice.dtos.urlaubsdaten;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UrlaubeListDto {
    private UrlaubTopLevelDto overviewData;
    private List<UrlaubDto> urlaubList;
}
