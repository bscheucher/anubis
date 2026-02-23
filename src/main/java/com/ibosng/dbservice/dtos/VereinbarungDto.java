package com.ibosng.dbservice.dtos;

import com.ibosng.dbservice.dtos.workflows.WorkflowItemDto;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VereinbarungDto {

    private Integer id;
    private String vereinbarungName;
    private String firmaName;
    private String personalnummer;
    private String nachname;
    private String vorname;
    private String fuehrungskraft;
    private VereinbarungStatus status;
    private LocalDate gueltigAb;
    private LocalDate gueltigBis;
    private List<VereinbarungParameterDto> parameters;
    private LocalDateTime createdOn;
    private String createdBy;
    private LocalDateTime changedOn;
    private String changedBy;
    private WorkflowItemDto workflowItem;
    private Integer workflowId;

}

