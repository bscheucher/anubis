package com.ibosng.dbservice.entities.zeiterfassung;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "auszahlungsantrag")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Auszahlungsantrag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "personalnummer_id", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private AuszahlungsantragStatus status;

    @ManyToOne
    @JoinColumn(name = "zeitspeicher_id", referencedColumnName = "id")
    private Zeitspeicher zeitspeicher;

    @Column(name = "anfrage_nummer")
    private Integer anfrageNr;

    @Column(name = "anzahl_minuten")
    private Long anzahlMinuten;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

}
