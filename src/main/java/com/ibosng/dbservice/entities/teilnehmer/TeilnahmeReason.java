package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "teilnahme_reason")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeilnahmeReason {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "kuerzel")
    private String kuerzel;

    @Column(name = "bezeichnung")
    private String bezeichnung;

    @Column(name = "an_abwesenheit")
    private Boolean anAbwesenheit;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "TeilnahmeReason{" +
                "id=" + id +
                ", kuerzel='" + kuerzel + '\'' +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", anAbwesenheit=" + anAbwesenheit +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
}
