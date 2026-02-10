package com.ibosng.dbservice.entities.zeiterfassung;

import com.ibosng.dbservice.entities.seminar.Seminar;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "zeiterfassung_transfer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZeiterfassungTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ZeiterfassungStatus status;

    @Column(name = "datum_von")
    private LocalDate datumVon;

    @Column(name = "datum_bis")
    private LocalDate datumBis;

    @Column(name = "datum_sent")
    private LocalDateTime datumSent;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinTable(
            name = "zeiterfassung_transfer_seminar",
            joinColumns = @JoinColumn(name = "zeiterfassung_transfer_id"),
            inverseJoinColumns = @JoinColumn(name = "seminar_id")
    )
    private List<Seminar> seminars = new ArrayList<>();

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "ZeiterfassungTransfer{" +
                "id=" + id +
                ", status=" + status +
                ", datumVon=" + datumVon +
                ", datumBis=" + datumBis +
                ", datumSent=" + datumSent +
                ", seminars=" + seminars.stream().map(Seminar::getBezeichnung).toList() +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ZeiterfassungTransfer that = (ZeiterfassungTransfer) object;
        return status == that.status &&
                Objects.equals(datumVon, that.datumVon) &&
                Objects.equals(datumBis, that.datumBis) &&
                Objects.equals(datumSent, that.datumSent) &&
                Objects.equals(seminars.stream().map(Seminar::getId).toList(), that.seminars.stream().map(Seminar::getId).toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, datumVon, datumBis, datumSent, seminars.stream().map(Seminar::getId).toList());
    }
}
