package com.ibosng.dbservice.dtos;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "nachname", "vorname", "svnr", "gerburtsdatum", "ursprungsland", "gerburtsort",
        "ziel", "vermittelbarAb", "notiz"})
@Builder(toBuilder = true)
public class TeilnehmerCsvDto {
    @CsvBindByName(column = "ID")
    @JsonSetter("ID")
    private Integer id;

    @CsvBindByName(column = "Nachname")
    @JsonSetter("Nachname")
    private String nachname;

    @CsvBindByName(column = "Vorname")
    @JsonSetter("Vorname")
    private String vorname;

    @CsvBindByName(column = "SVnr")
    @JsonSetter("SVnr")
    private String svnr;

    @CsvBindByName(column = "Gerburtsdatum")
    @JsonSetter("Gerburtsdatum")
    private String gerburtsdatum;

    @CsvBindByName(column = "Ursprungsland")
    @JsonSetter("Ursprungsland")
    private String ursprungsland;

    @CsvBindByName(column = "Gerburtsort")
    @JsonSetter("Gerburtsort")
    private String gerburtsort;

    @CsvBindByName(column = "Erläuterung ziel")
    @JsonSetter("Erläuterung ziel")
    private String ziel;

    @CsvBindByName(column = "Vermittelbar ab")
    @JsonSetter("Vermittelbar ab")
    private String vermittelbarAb;

    @CsvBindByName(column = "Notiz")
    @JsonSetter("Notiz")
    private String notiz;
}
