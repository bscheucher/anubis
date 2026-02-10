package com.ibosng.dbservice.dtos.moxis;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper=true)
public class MoxisUserDto extends BasicMoxisUserDto {
    private String classifier;
    private String roleName;
    private DirectSignaturePositionDto directUserPosition;
    private String action;
    private OffsetDateTime actionDate;
}
