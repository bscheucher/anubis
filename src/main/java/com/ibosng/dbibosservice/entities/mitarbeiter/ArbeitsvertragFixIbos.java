package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ARBEITSVERTRAG_FIX")
@Data
public class ArbeitsvertragFixIbos {

    @Id
    @Column(name = "ARBEITSVERTRAG_id", nullable = false, updatable = false)
    private Integer arbeitsvertragId;

    @Column(name = "avab", columnDefinition = "TINYINT(1)")
    private Boolean avab;

    @Column(name = "pendlerp", columnDefinition = "TINYINT(1)")
    private Boolean pendlerp;

    @Column(name = "fsb_kopie_uebergeben", columnDefinition = "TINYINT(1)")
    private Boolean fsbKopieUebergeben;

    @Column(name = "betriebsvereinbarung", columnDefinition = "TINYINT(1)")
    private Boolean betriebsvereinbarung;

    @Column(name = "reisekostenvereinbarung", columnDefinition = "TINYINT(1)")
    private Boolean reisekostenvereinbarung;

    @Column(name = "gleitzeitvereinbarung", columnDefinition = "TINYINT(1)")
    private Boolean gleitzeitvereinbarung;

    @Column(name = "gleitzeitvereinbarung_light", columnDefinition = "TINYINT(1)")
    private Boolean gleitzeitvereinbarungLight;

    @Column(name = "mobileworkingvereinbarung", columnDefinition = "TINYINT(1)")
    private Boolean mobileworkingvereinbarung;

    @Column(name = "unterweisung_arbeitssicherheit", columnDefinition = "TINYINT(1)")
    private Boolean unterweisungArbeitssicherheit;

    @Column(name = "vdgeprueft", columnDefinition = "TINYINT(1)")
    private Boolean vdGeprueft;

    @Column(name = "startcoach_id", columnDefinition = "INT(6) UNSIGNED")
    private Integer startcoachId;

    @Column(name = "vorgesetzter_id", columnDefinition = "INT(6) UNSIGNED")
    private Integer vorgesetzterId;

    @Column(name = "ansaessbesch", columnDefinition = "TINYINT(1)")
    private Boolean ansaessBesch;

    @Column(name = "zulage_berechnen", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean zulageBerechnen;

    @Column(name = "vdz_monate_nichterfasst", precision = 6, scale = 2, columnDefinition = "DECIMAL(6, 2)")
    private BigDecimal vdzMonateNichtErfasst;
}
