package com.ibosng.dbservice.entities.urlaub;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "anspruch")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Anspruch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "lhr_id")
    private Integer lhrId;

    @Column(name = "bezeichnung")
    private String bezeichnung;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Anspruch anspuruch)) return false;
        return Objects.equals(getId(), anspuruch.getId()) && Objects.equals(getBezeichnung(), anspuruch.getBezeichnung());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getBezeichnung());
    }

    @Override
    public String toString() {
        return "Anspuruch{" +
                "id=" + id +
                ", bezeichnung='" + bezeichnung + '\'' +
                '}';
    }
}
