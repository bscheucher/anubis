package com.ibosng.dbibosservice.entities.teilnahme;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "TEILNAHME")
@Data
public class Teilnahme {
    @EmbeddedId
    private TeilnahmeId id;

    @Column(name = "TNdatumBis")
    private LocalDate tnDatumBis;

    @Column(name = "TNletzterKurstag")
    private LocalDate tnLetzterKurstag;

    @Column(name = "TNstatusV")
    private Character tnStatusV;

    @Column(name = "TNstatusN")
    private Character tnStatusN;

    @Column(name = "KSTTNstatusV")
    private Integer ksttnStatusV;

    @Column(name = "KSTTNstatusN")
    private Integer ksttnStatusN;

    @Column(name = "TNSTATUS_id")
    private Integer tnStatusId;

    @Column(name = "TNbemerkung")
    private String tnBemerkung;

    @Column(name = "TNstatus")
    private Character tnStatus;

    @Column(name = "TNaeda")
    private LocalDateTime tnaeda;

    @Column(name = "TNaeuser", columnDefinition = "char(35)")
    private String tnaeuser;

    @Column(name = "TNerda")
    private LocalDateTime tnerda;

    @Column(name = "TNeruser", columnDefinition = "char(35)")
    private String tnEruser;
}
