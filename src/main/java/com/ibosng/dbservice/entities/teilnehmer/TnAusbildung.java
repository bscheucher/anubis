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
@Table(name = "tn_ausbildung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TnAusbildung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teilnehmer")
    private Teilnehmer teilnehmer;

    @ManyToOne
    @JoinColumn(name = "ausbildung_type")
    private TnAusbildungType ausbildungType;

    @Column(name = "hoechster_abschluss")
    private Boolean hoechsterAbschluss = false;

    @Column(name = "erkannt_in_at")
    private Boolean erkanntInAt = false;

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
        TnAusbildung that = (TnAusbildung) o;
        return Objects.equals(teilnehmer != null ? teilnehmer.getId() : null, that.teilnehmer != null ? that.teilnehmer.getId() : null) &&
                Objects.equals(ausbildungType != null ? ausbildungType.getId() : null, that.ausbildungType != null ? that.ausbildungType.getId() : null) &&
                Objects.equals(hoechsterAbschluss, that.hoechsterAbschluss) &&
                Objects.equals(erkanntInAt, that.erkanntInAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teilnehmer != null ? teilnehmer.getId() : null,
                ausbildungType != null ? ausbildungType.getId() : null,
                hoechsterAbschluss,
                erkanntInAt);
    }

    @Override
    public String toString() {
        return "TnAusbildung{" +
                "teilnehmer=" + (teilnehmer != null ? teilnehmer.getId() : "null") +
                ", ausbildungType=" + (ausbildungType != null ? ausbildungType.getId() : "null") +
                ", hoechsterAbschluss=" + hoechsterAbschluss +
                ", erkanntInAt=" + erkanntInAt +
                '}';
    }
}
