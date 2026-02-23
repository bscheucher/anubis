package com.ibosng.dbservice.entities.zeiterfassung;

import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "zeiterfassung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Zeiterfassung {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teilnehmer")
    private Teilnehmer teilnehmer;

    @ManyToOne
    @JoinColumn(name = "seminar")
    private Seminar seminar;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zeiterfassung_reason")
    private ZeiterfassungReason zeiterfassungReason;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "zeiterfassung_transfer")
    private ZeiterfassungTransfer zeiterfassungTransfer;

    private LocalDate datum;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ZeiterfassungStatus status;

    @Column(name = "datum_bis")
    private LocalDate datumBis;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "bemerkung")
    private String bemerkung;

    @Override
    public String toString() {
        return "Zeiterfassung{" +
                "id=" + id +
                ", teilnehmer=" + (teilnehmer != null ? teilnehmer.getId() : "null") +
                ", zeiterfassungReason=" + (zeiterfassungReason != null ? zeiterfassungReason.getBezeichnung() : "null") +
                ", zeiterfassungTransfer=" + (zeiterfassungTransfer != null ? zeiterfassungTransfer.getId() : "null") +
                ", datum=" + datum +
                ", datumBis=" + datumBis +
                ", bemerkung='" + bemerkung + '\'' +
                ", createdOn=" + createdOn +
                ", changedOn=" + changedOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zeiterfassung that)) return false;
        return getId() == that.getId() &&
                Objects.equals(getTeilnehmer(), that.getTeilnehmer()) &&
                Objects.equals(getZeiterfassungReason(), that.getZeiterfassungReason()) &&
                Objects.equals(getZeiterfassungTransfer(), that.getZeiterfassungTransfer()) &&
                Objects.equals(getDatum(), that.getDatum()) &&
                Objects.equals(bemerkung, that.bemerkung) &&
                Objects.equals(getDatumBis(), that.getDatumBis());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTeilnehmer(), getZeiterfassungReason(), getZeiterfassungTransfer(), getDatum(), getDatumBis(), getBemerkung());
    }
}
