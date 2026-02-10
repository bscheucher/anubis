package com.ibosng.dbibosservice.entities.smtn;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class SmTnId implements Serializable {

    @Column(name = "ADRESSE_ADadnr")
    private Integer adresseAdnr;

    @Column(name = "SEMINAR_SMnr", nullable = false)
    private Integer seminarSmnr;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmTnId that)) {
            return false;
        }
        return Objects.equals(adresseAdnr, that.getAdresseAdnr()) &&
                Objects.equals(seminarSmnr, that.getSeminarSmnr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(adresseAdnr, seminarSmnr);
    }
}
