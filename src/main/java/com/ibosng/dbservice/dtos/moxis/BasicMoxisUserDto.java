package com.ibosng.dbservice.dtos.moxis;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

@Data
public class BasicMoxisUserDto {
    private String name;
    @JsonSetter("@class")
    private String userClass;
}
