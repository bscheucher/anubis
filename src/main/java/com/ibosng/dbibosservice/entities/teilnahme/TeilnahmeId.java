package com.ibosng.dbibosservice.entities.teilnahme;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class TeilnahmeId implements Serializable {
    @Column(name = "SM_TN_ADRESSE_ADadnr")
    private Integer adresseAdnr;

    @Column(name = "SM_TN_SEMINAR_SMnr", nullable = false)
    private Integer seminarSmnr;

    @Column(name = "TNdatum")
    private LocalDate datum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeilnahmeId that)) return false;
        return Objects.equals(getAdresseAdnr(), that.getAdresseAdnr()) && Objects.equals(getSeminarSmnr(), that.getSeminarSmnr()) && Objects.equals(getDatum(), that.getDatum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAdresseAdnr(), getSeminarSmnr(), getDatum());
    }
}
