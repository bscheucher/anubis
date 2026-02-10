package com.ibosng.dbservice.entities.lhr;

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
@Table(name = "gehaltsinfo")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gehaltsinfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "kz")
    private String kz;

    @Column(name = "name")
    private String name;

    @Column(name = "passwort")
    private String passwort;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Gehaltsinfo{" +
                "kz='" + kz + '\'' +
                ", name='" + name + '\'' +
                ", passwort='" + passwort + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gehaltsinfo that = (Gehaltsinfo) o;
        return Objects.equals(kz, that.kz) && Objects.equals(name, that.name) && Objects.equals(passwort, that.passwort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kz, name, passwort);
    }
}
