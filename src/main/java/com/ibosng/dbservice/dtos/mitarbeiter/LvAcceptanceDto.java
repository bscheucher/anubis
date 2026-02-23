package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LvAcceptanceDto {
    private String personalnummer;
    private boolean bankcard;
    private String bankcardReason;
    private boolean ecard;
    private String ecardReason;
    private boolean arbeitsgenehmigungDok;
    private String arbeitsgenehmigungDokReason;
    private boolean gehaltEinstufung;
    private String gehaltEinstufungReason;
    private boolean hasArbeitsgenehmigung = false;
    private boolean needsArbeitsgenehmigung = false;
    private boolean hasBankcard = true;
    private boolean hasEcard = true;
}
