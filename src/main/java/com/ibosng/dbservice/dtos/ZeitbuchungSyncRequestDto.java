package com.ibosng.dbservice.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ZeitbuchungSyncRequestDto {
    private Integer personalnummerId;
    private String startDate;
    private String endDate;
}
