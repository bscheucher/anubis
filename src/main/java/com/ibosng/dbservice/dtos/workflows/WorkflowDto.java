package com.ibosng.dbservice.dtos.workflows;

import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowDto {
    private Integer workflowId;
    private Integer referenceWorkflowId;
    private String workflowName;
    private WWorkflowStatus workflowStatus;
    private String data;
    private String changedBy;
    private LocalDateTime changedOn;
    List<WorkflowItemDto> workflowItems = new ArrayList<>();
}
