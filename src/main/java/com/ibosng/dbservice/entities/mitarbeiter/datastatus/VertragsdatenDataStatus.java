package com.ibosng.dbservice.entities.mitarbeiter.datastatus;

import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
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
@DiscriminatorValue("VertragsdatenDataStatus")
public class VertragsdatenDataStatus extends MitarbeiterDataStatus{

    @ManyToOne
    @JoinColumn(name = "vertragsdaten")
    private Vertragsdaten vertragsdaten;

    @Override
    public String toString() {
        return "VertragsdatenDataStatus{" +
                "vertragsdaten=" + vertragsdaten != null ? String.valueOf(vertragsdaten.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        VertragsdatenDataStatus that = (VertragsdatenDataStatus) object;
        return Objects.equals(vertragsdaten != null ? vertragsdaten.getId() : null, that.vertragsdaten != null ? that.vertragsdaten.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), vertragsdaten != null ? vertragsdaten.getId() : null);
    }
}
