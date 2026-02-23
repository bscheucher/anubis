package com.ibosng.natifservice.dtos.extractions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class DetailsDto {

    @JsonProperty("bbox_refs")
    private List<BboxRefsDto> bboxRefs;

    private String value;

    @JsonProperty("validation_problem")
    private Boolean validationProblem;

    private String note;
    private Double confidence;
}
