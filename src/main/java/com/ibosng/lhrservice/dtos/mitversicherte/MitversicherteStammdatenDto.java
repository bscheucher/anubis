package com.ibosng.lhrservice.dtos.mitversicherte;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ibosng.lhrservice.dtos.GruppeNameKzDto;
import lombok.Data;

import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MitversicherteStammdatenDto {
    private String validFrom;
    private String validTo;
    private String name;
    private String vorname;
    private GruppeNameKzDto verwandt;
    private GeschlechtDto geschlecht;
    private String staat;
    private String geburtsdatum;
    private String svNummer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MitversicherteStammdatenDto that = (MitversicherteStammdatenDto) o;
        return Objects.equals(name, that.name) && Objects.equals(vorname, that.vorname) && Objects.equals(verwandt, that.verwandt) && Objects.equals(geschlecht, that.geschlecht) && Objects.equals(staat, that.staat) && Objects.equals(geburtsdatum, that.geburtsdatum) && Objects.equals(svNummer, that.svNummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, vorname, verwandt, geschlecht, staat, geburtsdatum, svNummer);
    }
}
