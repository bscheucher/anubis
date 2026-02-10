package com.ibosng.dbservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ZeitbuchungMetadataDto {

    private String lastSyncedAt;
    private boolean umbuchungAvailable;

}
