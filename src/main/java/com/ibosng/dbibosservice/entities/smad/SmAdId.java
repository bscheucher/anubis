package com.ibosng.dbibosservice.entities.smad;

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
public class SmAdId implements Serializable {

    @Column(name = "SMADnr", nullable = false)
    private Integer smadnr;

    @Column(name = "SEMINAR_SMnr", nullable = false)
    private Integer seminarSmnr;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SmAdId that)) {
            return false;
        }
        return Objects.equals(smadnr, that.getSmadnr()) &&
                Objects.equals(seminarSmnr, that.getSeminarSmnr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(smadnr, seminarSmnr);
    }
}
