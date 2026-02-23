package com.ibosng.dbservice.entities.telefon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Land;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "telefon")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Telefon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "land_id")
    private Land land;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private TelefonStatus status;

    @Column(name = "telefonnummer")
    private Long telefonnummer;

    @Column(name = "owner")
    private String owner;

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
        return "Telefon{" +
                "id=" + id +
                ", land=" + land +
                ", status=" + status +
                ", telefonnummer=" + telefonnummer +
                ", owner='" + owner + '\'' +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Telefon telefon = (Telefon) object;
        return Objects.equals(land, telefon.land) &&
                Objects.equals(telefonnummer, telefon.telefonnummer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(land, telefonnummer);
    }

    public Telefon(Land land, TelefonStatus status, Long telefonnummer, String createdBy) {
        this.land = land;
        this.status = status;
        this.telefonnummer = telefonnummer;
        this.createdBy = createdBy;
    }
}
