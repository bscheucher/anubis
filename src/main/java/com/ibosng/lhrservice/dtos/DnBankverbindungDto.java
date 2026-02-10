package com.ibosng.lhrservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DnBankverbindungDto {

    private DnAuftraggeberBankDto auftraggeberBank;
    private String bankbezeichnung;
    private String bankleitzahl;
    private Boolean barBeiAustritt;
    private String ibanOrKtoNummer;
    private String land;
    private String lautendAuf;
}
