package com.ibosng.natifservice.dtos.extractions.kompetenzen;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeilDerBerufsobergruppeDto extends CategorizedKompetenz {

    public TeilDerBerufsobergruppeDto() {
        this.setArt("teil_der_berufsobergruppe");
    }
}
