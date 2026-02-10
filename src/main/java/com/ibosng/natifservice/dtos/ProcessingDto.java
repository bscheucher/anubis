package com.ibosng.natifservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibosng.natifservice.dtos.extractions.ExtractionsDto;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingDto {

    @JsonProperty("processing_id")
    String processingId;

    @JsonProperty("workflow_id")
    String workflowId;

    @JsonProperty("workflow_name")
    String workflowName;

    @JsonProperty("avaliable_results")
    List<String> availableResults;

    @JsonProperty("extractions")
    ExtractionsDto extractionsDto;
}
