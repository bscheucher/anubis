package com.ibosng.dbservice.dtos.moxis;

import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class Signer {

    private String classifier;
    private String name;
    @JsonSetter("@class")
    private String userClass;
    private String action;
    private OffsetDateTime actionDate;
    private List<String> comments;
}
