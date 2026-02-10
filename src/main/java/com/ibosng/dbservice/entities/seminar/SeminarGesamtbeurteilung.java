package com.ibosng.dbservice.entities.seminar;

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
@Table(name = "seminar_gesamtbeurteilung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeminarGesamtbeurteilung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "id")
    private SeminarPruefungGegenstand type;

    @ManyToOne
    @JoinColumn(name = "ergebnis", referencedColumnName = "id")
    private SeminarPruefungErgebnisType ergebnis;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeminarGesamtbeurteilung that = (SeminarGesamtbeurteilung) o;
        return Objects.equals(type != null ? type.getId() : null, that.type != null ? that.type.getId() : null) &&
                Objects.equals(ergebnis != null ? ergebnis.getId() : null, that.ergebnis != null ? that.ergebnis.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                type != null ? type.getId() : null,
                ergebnis != null ? ergebnis.getId() : null
        );
    }

    @Override
    public String toString() {
        return "SeminarGesamtbeurteilung{" +
                ", type=" + (type != null ? type.getName() : "null") +
                ", ergebnis=" + (ergebnis != null ? ergebnis.getName() : "null") +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
}
