package com.ibosng.dbservice.entities.teilnehmer;

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
@Table(name = "teilnehmer_2_wunschberuf")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Teilnehmer2Wunschberuf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teilnehmer_id")
    private Teilnehmer teilnehmer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wunschberuf_id")
    private Beruf wunschberuf;

    @JsonIgnore
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
    public String toString() {
        return "Teilnehmer2Wunschberuf{" +
                "id=" + id +
                ", teilnehmer=" + (teilnehmer != null ? teilnehmer.getId() : null) +
                ", wunschberuf=" + (wunschberuf != null ? wunschberuf.getId() : null) +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teilnehmer2Wunschberuf that = (Teilnehmer2Wunschberuf) o;
        return Objects.equals(teilnehmer != null ? teilnehmer.getId() : null,
                that.teilnehmer != null ? that.teilnehmer.getId() : null) &&
                Objects.equals(wunschberuf != null ? wunschberuf.getId() : null,
                        that.wunschberuf != null ? that.wunschberuf.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                teilnehmer != null ? teilnehmer.getId() : null,
                wunschberuf != null ? wunschberuf.getId() : null
        );
    }
}
