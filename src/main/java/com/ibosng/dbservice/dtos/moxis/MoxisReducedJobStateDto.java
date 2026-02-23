package com.ibosng.dbservice.dtos.moxis;

import lombok.Data;

@Data
public class MoxisReducedJobStateDto {

    private String state;
    private String processInstanceId;
}
