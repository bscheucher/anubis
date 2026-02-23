package com.ibosng.dbibosservice.entities.pzleistung;

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
public class PzleistungId {
    @Column(name = "ADadnr")
    private Integer ADadnr;

    @Column(name = "LZnr")
    private Integer LZnr;

    @Column(name = "PMjahr")
    private Integer PMjahr;

    @Column(name = "PMmonat")
    private Integer PMmonat;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PzleistungId that)) return false;
        return Objects.equals(getADadnr(), that.getADadnr()) && Objects.equals(getLZnr(), that.getLZnr()) && Objects.equals(getPMjahr(), that.getPMjahr()) && Objects.equals(getPMmonat(), that.getPMmonat());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getADadnr(), getLZnr(), getPMjahr(), getPMmonat());
    }
}
