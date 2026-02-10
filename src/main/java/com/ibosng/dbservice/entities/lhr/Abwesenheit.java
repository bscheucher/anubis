package com.ibosng.dbservice.entities.lhr;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "abwesenheit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Abwesenheit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @Column(name = "grund")
    private String grund;

    @Column(name = "beschreibung")
    private String beschreibung;

    @Column(name = "kommentar")
    private String kommentar;

    @Column(name = "art")
    private String art;

    @Column(name = "id_lhr")
    private Integer idLhr;

    @Column(name = "von")
    private LocalDate von;

    @Column(name = "bis")
    private LocalDate bis;

    @Column(name = "typ")
    private String typ;

    @Column(name = "tage")
    private Double tage;

    @Column(name = "saldo")
    private Double saldo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private AbwesenheitStatus status;

    @Column(name = "comment_fuehrungskraft")
    private String commentFuehrungskraft;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "abwesenheit_fuehrungskraft",
            joinColumns = @JoinColumn(name = "abwesenheit_id"),
            inverseJoinColumns = @JoinColumn(name = "benutzer_id")
    )
    private Set<Benutzer> fuehrungskraefte = new HashSet<>(); // Managers associated with this absence

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "verbaucht")
    private Double verbaucht; //lhr-konsum from start of the year until bis-date of abw

    @Column(name = "lhr_http_status")
    private Integer lhrHttpStatus;
}
