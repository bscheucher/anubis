package com.ibosng.dbservice.entities.masterdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ibis_firma_2_kostenstelle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IbisFirma2Kostenstelle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ibis_firma")
    private IbisFirma ibisFirma;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kostenstelle")
    private Kostenstelle kostenstelle;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    LocalDateTime createdOn = LocalDateTime.now();

    @JsonIgnore
    @Column(name = "created_by")
    String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    String changedBy;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    LocalDateTime changedOn = LocalDateTime.now();

    @Override
    public String toString() {
        return "IbisFirma2Kostenstelle{" +
                "id=" + id +
                ", ibisFirma=" + ibisFirma != null ? String.valueOf(ibisFirma.getId()) : "" +
                ", kostenstelle=" + kostenstelle != null ? String.valueOf(kostenstelle.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        IbisFirma2Kostenstelle that = (IbisFirma2Kostenstelle) object;
        return Objects.equals(ibisFirma != null ? ibisFirma.getId() : null, that.ibisFirma != null ? that.ibisFirma.getId() : null) &&
                Objects.equals(kostenstelle != null ? kostenstelle.getId() : null, that.kostenstelle != null ? that.kostenstelle.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ibisFirma != null ? ibisFirma.getId() : null, kostenstelle != null ? kostenstelle.getId() : null);
    }
}
