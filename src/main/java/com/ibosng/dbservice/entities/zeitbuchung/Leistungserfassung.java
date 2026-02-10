package com.ibosng.dbservice.entities.zeitbuchung;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "leistungserfassung")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Leistungserfassung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @Column(name = "leistungsdatum")
    private LocalDate leistungsdatum;

    @Enumerated(EnumType.STRING)
    @Column(name = "leistungstyp")
    private Leistungstyp leistungstyp;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "is_synced")
    private Boolean isSyncedWithLhr;

    @Column(name = "moxis_status")
    @Enumerated(EnumType.STRING)
    private MoxisStatus moxisStatus;

    @Column(name = "created_on")
    @Builder.Default
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Leistungserfassung{" +
                "id=" + id +
                ", personalnummer=" + ((personalnummer != null) ? personalnummer.getPersonalnummer() : "null") +
                ", leistungsdatum=" + leistungsdatum +
                ", leistungstyp=" + leistungstyp +
                ", isLocked=" + isLocked +
                ", isSyncedWithLhr=" + isSyncedWithLhr +
                ", moxisStatus=" + moxisStatus +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Leistungserfassung that)) return false;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getPersonalnummer(), that.getPersonalnummer()) && Objects.equals(getLeistungsdatum(), that.getLeistungsdatum()) && getLeistungstyp() == that.getLeistungstyp() && Objects.equals(getIsLocked(), that.getIsLocked()) && Objects.equals(getIsSyncedWithLhr(), that.getIsSyncedWithLhr()) && getMoxisStatus() == that.getMoxisStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPersonalnummer(), getLeistungsdatum(), getLeistungstyp(), getIsLocked(), getIsSyncedWithLhr(), getMoxisStatus());
    }
}
