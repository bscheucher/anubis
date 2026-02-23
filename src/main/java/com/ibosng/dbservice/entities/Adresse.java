package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "adresse")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ort")
    private String ort;

    @ManyToOne
    @JoinColumn(name = "plz")
    private BasePlz plz;

    @Column(name = "strasse")
    private String strasse;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "land")
    private Land land;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Adresse{" +
                "id=" + id +
                ", ort='" + ort + '\'' +
                ", plz=" + plz != null ? String.valueOf(plz.getPlzString()) : "" +
                ", strasse='" + strasse + '\'' +
                ", status=" + status +
                ", land=" + land != null ? land.getLandName() : "" +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Adresse adresse = (Adresse) object;
        return Objects.equals(ort, adresse.ort) &&
                Objects.equals(plz != null ? plz.getId() : null, adresse.plz != null ? adresse.plz.getId() : null) &&
                Objects.equals(strasse, adresse.strasse) &&
                Objects.equals(land, adresse.land);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ort, plz != null ? plz.getId() : null, strasse, land);
    }

    public Adresse(String ort, Plz plz, String strasse, String createdBy) {
        this.ort = ort;
        this.plz = plz;
        this.strasse = strasse;
        this.createdBy = createdBy;
    }
}
