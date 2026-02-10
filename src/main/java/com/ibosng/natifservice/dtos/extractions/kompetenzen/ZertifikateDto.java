package com.ibosng.natifservice.dtos.extractions.kompetenzen;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZertifikateDto extends CategorizedKompetenz{

    public ZertifikateDto() {
        this.setArt("zertifikate");
    }
}
