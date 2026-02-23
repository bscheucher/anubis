package com.ibosng.natifservice.dtos.extractions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ibosng.natifservice.dtos.extractions.kompetenzen.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtractionsDto {

    @JsonProperty("schema_version")
    private Integer schemaVersion;

    @JsonProperty("document_type")
    private String documentType;

    private String kostentraeger;

    private String nachname;

    private String vorname;

    @JsonProperty("sv_nr")
    private Integer svNr;

    private LocalDate geburtsdatum;

    @JsonProperty("pin_code")
    private DetailsDto pinCode;

    private DetailsDto tagesdatum;

    //region Art (Results of Sucheingaben)

    @JsonProperty("ams_6_steller")
    private List<AMSSechsStellerDto> amsSechsSteller;

    private List<FachlichDto> fachlich;

    private List<InteressensgebietDto> interessengebiete;

    private List<SuchbegriffeDto> suchbegriffe;

    @JsonProperty("teil_der_berufsobergruppe")
    private List<TeilDerBerufsobergruppeDto> teilDerBerufsobergruppe;

    private List<UeberfachlichDto> ueberfachlich;

    private List<VoraussetzungDto> voraussetzung;

    private List<ZertifikateDto> zertifikate;
    //endregion
}
