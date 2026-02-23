package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "vertragsaenderung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vertragsaenderung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personalnummer")
    private Personalnummer personalnummer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "vertragsaenderung_2_recipients",
            joinColumns = @JoinColumn(name = "vd_id"),
            inverseJoinColumns = @JoinColumn(name = "benutzer_id"))
    private Set<Benutzer> allRecipients = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "genehmigender")
    private Benutzer genehmigender;

    @ManyToOne
    @JoinColumn(name = "antragssteller")
    private Benutzer antragssteller;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "gueltig_ab")
    private LocalDate gueltigAb;

    @ManyToOne
    @JoinColumn(name = "successor")
    private Vertragsdaten successor;

    @ManyToOne
    @JoinColumn(name = "predecessor")
    private Vertragsdaten predecessor;

    @Column(name = "interne_anmerkung")
    private String interneAnmerkung;

    @Column(name = "offizielle_bemerkung")
    private String offizielleBemerkung;

    @Column(name = "kommentar")
    private String kommentar;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VertragsaenderungStatus status;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Vertragsaenderung{" +
                "personalnummer=" + personalnummer != null ? String.valueOf(personalnummer.getId()) : "" +
                ", antragssteller=" + antragssteller != null ? String.valueOf(antragssteller.getId()) : "" +
                ", gueltig_ab=" + gueltigAb +
                ", successor=" + successor != null ? String.valueOf(successor.getId()) : "" +
                ", predecessor=" + predecessor != null ? String.valueOf(predecessor.getId()) : "" +
                ", interne_anmerkung='" + interneAnmerkung + '\'' +
                ", offizielle_bemerkung='" + offizielleBemerkung + '\'' +
                ", kommentar='" + kommentar + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertragsaenderung that = (Vertragsaenderung) o;
        return Objects.equals(personalnummer != null ? personalnummer.getId() : null, that.personalnummer != null ? that.personalnummer.getId() : null) &&
                Objects.equals(antragssteller != null ? antragssteller.getId() : null, that.antragssteller != null ? that.antragssteller.getId() : null) &&
                Objects.equals(gueltigAb, that.gueltigAb) &&
                Objects.equals(successor != null ? successor.getId() : null, that.successor != null ? that.successor.getId() : null) &&
                Objects.equals(predecessor != null ? predecessor.getId() : null, that.predecessor != null ? that.predecessor.getId() : null) &&
                Objects.equals(interneAnmerkung, that.interneAnmerkung) &&
                Objects.equals(offizielleBemerkung, that.offizielleBemerkung) &&
                Objects.equals(kommentar, that.kommentar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalnummer != null ? personalnummer.getId() : null,
                antragssteller != null ? antragssteller.getId() : null,
                gueltigAb,
                successor != null ? successor.getId() : null,
                predecessor != null ? predecessor.getId() : null,
                interneAnmerkung,
                offizielleBemerkung,
                kommentar);
    }
}
