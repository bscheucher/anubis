package com.ibosng.dbibosservice.entities.mitarbeiter;

import com.ibosng.dbibosservice.utils.YesNoConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ARBEITSVERTRAG")
public class ArbeitsvertragIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "ADRESSE_adnr")
    private Integer addressNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "art")
    private Art art;

    @Column(name = "dienstgeber")
    private Integer dienstgeber;

    @Column(name = "agen", length = 40)
    private String agen;

    @Column(name = "agen_bis")
    private LocalDate agenBis;

    @Column(name = "agenbh", length = 40)
    private String agenbh;

    @Column(name = "agen_kopie_uebergeben")
    private Boolean agenKopieUebergeben;

    @Column(name = "agen_unbefristet")
    private Boolean agenUnbefristet = false;

    @Column(name = "geschaeftsbereich")
    private Integer geschaeftsbereich;

    @Column(name = "geringfuegig_karenz")
    private Boolean geringfuegigKarenz;

    @Column(name = "kurzarbeit")
    private Boolean kurzarbeit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArbeitsvertragStatus status = ArbeitsvertragStatus.offen;

    @Column(name = "formblattA1")
    private Boolean formblattA1;

    @Column(name = "DVnr_old")
    private Integer dvnrOld;

    @Column(name = "PBid_old")
    private Integer pbidOld;

    @Convert(converter = YesNoConverter.class)
    @Column(name = "loek")
    private Boolean loek;

    @Column(name = "aeda")
    private LocalDateTime aeda;

    @Column(name = "aeuser", columnDefinition = "char(35)")
    private String aeuser;

    @Column(name = "eruser", columnDefinition = "char(35)")
    private String eruser;

    @Column(name = "erda")
    private LocalDateTime erda;

    public enum Art {
        fix, freio, freim
    }

    public enum ArbeitsvertragStatus {
        offen, zugewiesen, gesperrt
    }
}
