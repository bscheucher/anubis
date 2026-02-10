package com.ibosng.dbservice.dtos.moxis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class MoxisJobStateDto extends MoxisReducedJobStateDto{

    private String processId;
    private JobMetaDataDto jobMetaData;
    private OffsetDateTime creationDate;
    private OffsetDateTime endDate;
    private Integer currentIteration;
    private List<IterationDto> iterations;
    private List<AddressBookEntryDto> additionalRecipients;
    private MoxisUserDto owner;
    private Map<String, String> customMap;
}
