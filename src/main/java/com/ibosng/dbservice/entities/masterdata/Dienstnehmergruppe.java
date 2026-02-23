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

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "dienstnehmergruppe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dienstnehmergruppe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "bezeichnung")
    private String bezeichnung;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Dienstnehmergruppe{" +
                "id=" + id +
                ", abbreviation='" + abbreviation + '\'' +
                ", bezeichnung='" + bezeichnung + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dienstnehmergruppe that = (Dienstnehmergruppe) o;
        return abbreviation.equals(that.abbreviation) && bezeichnung.equals(that.bezeichnung);
    }

    @Override
    public int hashCode() {
        return Objects.hash( abbreviation, bezeichnung);
    }
}
