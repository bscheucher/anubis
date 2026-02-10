package com.ibosng.dbservice.dtos.workflows;

import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowItemDto {
    private Integer workflowItemId;
    private Integer referenceWorkflowItemId;
    private String workflowItemName;
    private WWorkflowStatus workflowItemStatus;
    private String data;
    private LocalDateTime changedOn;
    private String changedBy;
}
