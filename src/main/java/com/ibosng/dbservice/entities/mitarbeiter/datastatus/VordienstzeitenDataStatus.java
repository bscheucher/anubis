package com.ibosng.dbservice.entities.mitarbeiter.datastatus;

import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("VordienstzeitenDataStatus")
public class VordienstzeitenDataStatus extends MitarbeiterDataStatus{

    @ManyToOne
    @JoinColumn(name = "vordienstzeiten")
    private Vordienstzeiten vordienstzeiten;


    @Override
    public String toString() {
        return "VordienstzeitenDataStatus{" +
                "vordienstzeiten=" + vordienstzeiten != null ? String.valueOf(vordienstzeiten.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        VordienstzeitenDataStatus that = (VordienstzeitenDataStatus) object;
        return Objects.equals(vordienstzeiten != null ? vordienstzeiten.getId() : null, that.vordienstzeiten != null ? that.vordienstzeiten.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vordienstzeiten != null ? vordienstzeiten.getId() : null);
    }
}
