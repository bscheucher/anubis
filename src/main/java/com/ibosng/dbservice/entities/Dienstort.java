package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.telefon.Telefon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "dienstort")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dienstort {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "lhr_kz")
    private String lhrKz;

    @Column(name = "lhr_nr")
    private Integer lhrNr;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "adresse")
    private Adresse adresse;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "telefon")
    private Telefon telefon;

    @Column(name = "email")
    private String email;

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
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Dienstort{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adresse=" + adresse != null ? String.valueOf(adresse.getId()) : "" +
                ", telefon=" + telefon != null ? String.valueOf(telefon.getId()) : "" +
                ", status=" + status +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Dienstort dienstort = (Dienstort) object;
        return Objects.equals(name, dienstort.name) &&
                Objects.equals(adresse != null ? adresse.getId() : null, dienstort.adresse != null ? dienstort.adresse.getId() : null) &&
                Objects.equals(telefon != null ? telefon.getId() : null, dienstort.telefon != null ? dienstort.telefon.getId() : null) &&
                Objects.equals(email, dienstort.email) &&
                status == dienstort.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, adresse != null ? adresse.getId() : null, telefon != null ? telefon.getId() : null, email, status);
    }
}
