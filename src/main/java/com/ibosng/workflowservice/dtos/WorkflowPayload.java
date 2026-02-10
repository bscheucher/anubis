package com.ibosng.workflowservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkflowPayload {
    private Integer workflowId;
    private String data;
}
