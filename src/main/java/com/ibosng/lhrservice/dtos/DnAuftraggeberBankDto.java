package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnAuftraggeberBankDto {

    private String firmenbankName;
    private Integer firmenbankNummer;
    private String zahlungsart;
}
