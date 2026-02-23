package com.ibosng.natifservice.dtos.extractions.kompetenzen;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class InteressensgebietDto extends CategorizedKompetenz{

    public InteressensgebietDto() {
        this.setArt("interessengebiete");
    }
}
