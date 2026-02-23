package com.ibosng.natifservice.dtos.extractions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BboxRefsDto {

    @JsonProperty("page_num")
    private Integer pageNumber;

    @JsonProperty("bbox_id")
    private Integer bboxId;

}
