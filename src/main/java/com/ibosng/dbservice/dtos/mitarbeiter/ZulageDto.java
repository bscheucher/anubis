package com.ibosng.dbservice.dtos.mitarbeiter;

import lombok.Data;

@Data
public class ZulageDto {

    private Integer id;
    private String gehaltInfoId;
    private String zulageInEuro;
    private String artDerZulage;

}
