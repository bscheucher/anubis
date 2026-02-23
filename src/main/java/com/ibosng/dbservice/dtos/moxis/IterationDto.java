package com.ibosng.dbservice.dtos.moxis;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class IterationDto {

    private Integer numberOfSignatures;
    private String category;
    private List<MoxisUserDto> invitees;
    private Integer iterationNumber;
    private String remark;
    private Boolean privateIteration;
    private String signerGroup;
    private DirectSignaturePositionDto groupPositions;
    private Map<String, String> customMap;
}
