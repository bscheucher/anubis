package com.ibosng.dbibosservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PzMonatId {

    @Column(name = "ADadnr")
    private Integer adAdnr;

    @Column(name = "PMjahr")
    private Integer jahr;

    @Column(name = "PMmonat")
    private Integer monat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PzMonatId pzMonatId)) return false;
        return Objects.equals(getAdAdnr(), pzMonatId.getAdAdnr()) && Objects.equals(getJahr(), pzMonatId.getJahr()) && Objects.equals(getMonat(), pzMonatId.getMonat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAdAdnr(), getJahr(), getMonat());
    }
}
