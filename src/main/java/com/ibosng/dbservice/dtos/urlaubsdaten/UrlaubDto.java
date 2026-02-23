package com.ibosng.dbservice.dtos.urlaubsdaten;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UrlaubDto {
    private String anspuruchType;
    private String month;
    private String fromDate;
}
