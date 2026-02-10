package com.ibosng.natifservice.dtos.extractions.kompetenzen;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class AMSSechsStellerDto extends CategorizedKompetenz{

    public AMSSechsStellerDto() {
        this.setArt("ams_6_steller");
    }
}
