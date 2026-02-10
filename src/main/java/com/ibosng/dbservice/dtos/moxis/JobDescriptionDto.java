package com.ibosng.dbservice.dtos.moxis;

import lombok.Data;

import java.util.List;

@Data
public class JobDescriptionDto {

    private BasicMoxisUserDto constituent;
    private String positionType;
    private JobMetaDataDto metaData;
    private List<IterationDto> iterationData;
    private List<AddressBookEntryDto> additionalRecipients;
}
