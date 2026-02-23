package com.ibosng.dbservice.entities.lhr;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "abmeldung")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Abmeldung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @Column(name = "austritts_datum")
    private LocalDate austrittsDatum;

    @ManyToOne
    @JoinColumn(name = "austrittsgrund", referencedColumnName = "id")
    private Austrittsgrund austrittsgrund;

    @Column(name = "bemerkung")
    private String bemerkung;

    @Column(name = "sv_nummer")
    private String svNummer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private AbmeldungStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow")
    private WWorkflow workflow;

}
