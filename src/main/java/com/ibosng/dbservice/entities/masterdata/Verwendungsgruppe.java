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
@Table(name = "verwendungsgruppe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Verwendungsgruppe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "lhr_klasse")
    private String lhrKlasse;

    @Column(name = "lhr_gruppe")
    private String lhrGruppe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kollektivvertrag")
    private Kollektivvertrag kollektivvertrag;

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
        return "Verwendungsgruppe{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lhr_klasse='" + lhrKlasse + '\'' +
                ", lhr_gruppe='" + lhrGruppe + '\'' +
                ", kollektivvertrag=" + kollektivvertrag != null ? String.valueOf(kollektivvertrag.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Verwendungsgruppe that = (Verwendungsgruppe) object;
        return Objects.equals(name, that.name) &&
                Objects.equals(kollektivvertrag != null ? kollektivvertrag.getId() : null, that.kollektivvertrag != null ? that.kollektivvertrag.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, kollektivvertrag != null ? kollektivvertrag.getId() : null);
    }
}
