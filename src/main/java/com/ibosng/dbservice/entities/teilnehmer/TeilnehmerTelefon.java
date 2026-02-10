package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.telefon.Telefon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "teilnehmer_telefon")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeilnehmerTelefon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teilnehmer_id")
    private Teilnehmer teilnehmer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "telefon_id")
    private Telefon telefon;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Override
    public String toString() {
        return "TeilnehmerTelefon{" +
                "id=" + id +
                ", teilnehmer=" + teilnehmer +
                ", telefon=" + telefon +
                ", createdOn=" + createdOn +
                '}';
    }
}
