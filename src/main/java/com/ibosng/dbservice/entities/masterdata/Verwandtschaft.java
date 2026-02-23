package com.ibosng.dbservice.entities.masterdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "verwandtschaft")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Verwandtschaft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "lhr_kz")
    private String lhrKz;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

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
        return "Klasse{" +
                "name='" + name + '\'' +
                ", lhrKz='" + lhrKz + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Verwandtschaft klasse = (Verwandtschaft) o;
        return Objects.equals(name, klasse.name) && Objects.equals(lhrKz, klasse.lhrKz) && status == klasse.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lhrKz, status);
    }

}
