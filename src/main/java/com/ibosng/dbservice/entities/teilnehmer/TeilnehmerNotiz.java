package com.ibosng.dbservice.entities.teilnehmer;

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
@Table(name = "teilnehmer_notiz")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeilnehmerNotiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teilnehmer", referencedColumnName = "id")
    private Teilnehmer teilnehmer;

    @Column(name = "notiz")
    private String notiz;

    @ManyToOne
    @JoinColumn(name = "kategorie", referencedColumnName = "id")
    private TeilnehmerNotizKategorie kategorie;

    @ManyToOne
    @JoinColumn(name = "type", referencedColumnName = "id")
    private TeilnehmerNotizType type;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeilnehmerNotiz that = (TeilnehmerNotiz) o;
        return Objects.equals(teilnehmer, that.teilnehmer) && Objects.equals(notiz, that.notiz) && Objects.equals(kategorie, that.kategorie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teilnehmer, notiz, kategorie);
    }

    @Override
    public String toString() {
        return "TeilnehmerNotiz{" +
                "teilnehmer=" + teilnehmer +
                ", notiz='" + notiz + '\'' +
                ", kategorie=" + kategorie +
                '}';
    }
}