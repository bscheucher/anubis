package com.ibosng.dbservice.entities.teilnehmer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ibosng.dbservice.entities.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "tn_teilnahme_status")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeilnahmeStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teilnehmer")
    private Teilnehmer teilnehmer;

    @ManyToOne
    @JoinColumn(name = "tn_teilnahme")
    private TeilnahmerTeilnahme teilnahmerTeilnahme;

    @ManyToOne
    @JoinColumn(name = "tn_reason")
    private TeilnahmeReason teilnahmeReason;

    @Column(name = "info")
    private String info;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

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
        return "TeilnahmeStatus{" +
                "id=" + id +
                ", teilnehmer=" + teilnehmer != null ? String.valueOf(teilnehmer.getId()) : "null" +
                ", teilnahmerTeilnahme=" + teilnahmerTeilnahme != null ? String.valueOf(teilnahmerTeilnahme.getId()) : "null" +
                ", teilnahmeReason='" + teilnahmeReason != null ? teilnahmeReason.getBezeichnung() : "null" + '\'' +
                ", info='" + info + '\'' +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
}
