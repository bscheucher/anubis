package com.ibosng.dbservice.entities.mitarbeiter.datastatus;

import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
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
@DiscriminatorValue("UnterhaltsberechtigteDataStatus")
public class UnterhaltsberechtigteDataStatus extends MitarbeiterDataStatus{

    @ManyToOne
    @JoinColumn(name = "unterhaltsberechtigte")
    private Unterhaltsberechtigte unterhaltsberechtigte;


    @Override
    public String toString() {
        return "UnterhaltsberechtigteDataStatus{" +
                "vordienstzeiten=" + unterhaltsberechtigte != null ? String.valueOf(unterhaltsberechtigte.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        UnterhaltsberechtigteDataStatus that = (UnterhaltsberechtigteDataStatus) object;
        return Objects.equals(unterhaltsberechtigte != null ? unterhaltsberechtigte.getId() : null, that.unterhaltsberechtigte != null ? that.unterhaltsberechtigte.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), unterhaltsberechtigte != null ? unterhaltsberechtigte.getId() : null);
    }
}
