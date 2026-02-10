package com.ibosng.dbservice.entities;

import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "zeitausgleich")
@Builder(toBuilder = true)
public class Zeitausgleich {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    private LocalDate datum;

    @Column(name = "time_von")
    private LocalTime timeVon;

    @Column(name = "time_bis")
    private LocalTime timeBis;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_on")
    @Builder.Default
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private AbwesenheitStatus status;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "zeitausgleich_fuehrungskraft",
            joinColumns = @JoinColumn(name = "zeitausgleich_id"),
            inverseJoinColumns = @JoinColumn(name = "benutzer_id")
    )
    private Set<Benutzer> fuehrungskraefte = new HashSet<>(); // Associated leaders/managers

    @Column(name = "comment_fuehrungskraft")
    private String commentFuehrungskraft;

    @Override
    public String toString() {
        return "Zeitausgleich{" +
                "timeBis=" + timeBis +
                ", timeVon=" + timeVon +
                ", datum=" + datum +
                ", personalnummer=" + personalnummer +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zeitausgleich that)) return false;
        return Objects.equals(getPersonalnummer(), that.getPersonalnummer()) && Objects.equals(getDatum(), that.getDatum()) && Objects.equals(getTimeVon(), that.getTimeVon()) && Objects.equals(getTimeBis(), that.getTimeBis());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonalnummer(), getDatum(), getTimeVon(), getTimeBis());
    }
}