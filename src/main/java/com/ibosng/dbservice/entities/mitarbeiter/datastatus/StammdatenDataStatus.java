package com.ibosng.dbservice.entities.mitarbeiter.datastatus;

import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
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
@DiscriminatorValue("StammdatenDataStatus")
public class StammdatenDataStatus extends MitarbeiterDataStatus{

    @ManyToOne
    @JoinColumn(name = "stammdaten")
    private Stammdaten stammdaten;


    @Override
    public String toString() {
        return "StammdatenDataStatus{" +
                "stammdaten=" + stammdaten != null ? String.valueOf(stammdaten.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        StammdatenDataStatus that = (StammdatenDataStatus) object;
        return Objects.equals(stammdaten != null ? stammdaten.getId() : null, that.stammdaten != null ? that.stammdaten.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), stammdaten != null ? stammdaten.getId() : null);
    }
}
