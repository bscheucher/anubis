package com.ibosng.dbservice.entities.zeiterfassung;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "zeiterfassung_reason")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZeiterfassungReason {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private String bezeichnung;

    @Column(name = "short_bezeichnung")
    private String shortBezeichnung;

    @Column(name = "lhr_kz")
    private String lhrKz;

    @Column(name = "ibos_id")
    private Integer ibosId;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "anwesend")
    private Boolean anwesend = false;

    @Override
    public String toString() {
        return "ZeiterfassungReason{" +
                "id=" + id +
                ", bezeichnung='" + bezeichnung + '\'' +
                ", anwesend=" + anwesend +
                ", shortBezeichnung='" + shortBezeichnung + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZeiterfassungReason that)) return false;
        return getId() == that.getId() && Objects.equals(getBezeichnung(), that.getBezeichnung()) && Objects.equals(getShortBezeichnung(), that.getShortBezeichnung()) && Objects.equals(getAnwesend(), that.getAnwesend());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBezeichnung(), getShortBezeichnung(), getAnwesend());
    }
}
