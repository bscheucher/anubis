package com.ibosng.dbservice.entities.urlaub;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "urlaubsdaten")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Urlaubsdaten {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @ManyToOne
    @JoinColumn(name = "anspruch_type", referencedColumnName = "id")
    private Anspruch anspruchType;

    @Column(name = "month")
    private LocalDate month;

    @Column(name = "from_date")
    private LocalDate from;

    @Column(name = "next_anspruch")
    private LocalDate nextAnspruch;

    @Column(name = "kuerzung")
    private Double kuerzung;

    @Column(name = "verjaehrung")
    private Double verjaehrung;

    @Column(name = "anspruch")
    private Double anspruch;

    @Column(name = "konsum")
    private Double konsum;

    @Column(name = "rest")
    private Double rest;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Urlaubsdaten{" +
                "personalnummer=" + personalnummer +
                ", anspruchType=" + anspruchType +
                ", month=" + month +
                ", from=" + from +
                ", nextAnspruch=" + nextAnspruch +
                ", kuerzung=" + kuerzung +
                ", verjaehrung=" + verjaehrung +
                ", anspruch=" + anspruch +
                ", konsum=" + konsum +
                ", rest=" + rest +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Urlaubsdaten that)) return false;
        return Objects.equals(getPersonalnummer(), that.getPersonalnummer()) && Objects.equals(getAnspruchType(), that.getAnspruchType()) && Objects.equals(getMonth(), that.getMonth()) && Objects.equals(getFrom(), that.getFrom()) && Objects.equals(getNextAnspruch(), that.getNextAnspruch()) && Objects.equals(getKuerzung(), that.getKuerzung()) && Objects.equals(getVerjaehrung(), that.getVerjaehrung()) && Objects.equals(getAnspruch(), that.getAnspruch()) && Objects.equals(getKonsum(), that.getKonsum()) && Objects.equals(getRest(), that.getRest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPersonalnummer(), getAnspruchType(), getMonth(), getFrom(), getNextAnspruch(), getKuerzung(), getVerjaehrung(), getAnspruch(), getKonsum(), getRest());
    }
}
