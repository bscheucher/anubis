package com.ibosng.dbservice.dtos.teilnahme;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class TeilnahmeOverviewDto {
    private TeilnahmeMetadataDto teilnahmeMetadata;
    private List<TeilnehmerTeilnahmeOverviewDto> teilnehmers;
}
