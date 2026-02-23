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
@Table(name = "kv_stufe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KVStufe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "min_months ")
    private Integer minMonths;

    @Column(name = "max_months ")
    private Integer maxMonths;

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
        return "KVStufe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minMonths=" + minMonths +
                ", maxMonths=" + maxMonths +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        KVStufe kvStufe = (KVStufe) object;
        return Objects.equals(name, kvStufe.name) && Objects.equals(minMonths, kvStufe.minMonths) && Objects.equals(maxMonths, kvStufe.maxMonths);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, minMonths, maxMonths);
    }

    public KVStufe(Integer id, String name, Integer minMonths, Integer maxMonths) {
        this.id = id;
        this.name = name;
        this.minMonths = minMonths;
        this.maxMonths = maxMonths;
    }
}
