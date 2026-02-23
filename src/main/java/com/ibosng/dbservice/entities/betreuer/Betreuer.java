package com.ibosng.dbservice.entities.betreuer;

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
@Table(name = "betreuer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Betreuer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @Column(name = "titel")
    private String titel;

    @Column(name = "nachname")
    private String nachname;

    @Column(name = "vorname")
    private String vorname;

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

    public Betreuer(Status status, String titel, String nachname, String vorname, String createdBy) {
        this.status = status;
        this.titel = titel;
        this.nachname = nachname;
        this.vorname = vorname;
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Betreuer{" +
                "id=" + id +
                ", status=" + status +
                ", titel='" + titel + '\'' +
                ", nachname='" + nachname + '\'' +
                ", vorname='" + vorname + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Betreuer betreuer = (Betreuer) o;
        return Objects.equals(titel, betreuer.titel) && Objects.equals(nachname, betreuer.nachname) && Objects.equals(vorname, betreuer.vorname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status, titel, nachname, vorname);
    }
}
