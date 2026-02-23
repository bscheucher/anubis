package com.ibosng.dbservice.dtos.moxis;

import com.ibosng.dbservice.entities.moxis.SigningJobType;
import lombok.Data;

import java.util.List;

@Data
public class MoxisJobDto {
    private Integer workflow;
    private String personalNummer;
    private String changedBy;
    private List<String> additionalRecipients;
    private String description;
    private SigningJobType signingJobType;
}
