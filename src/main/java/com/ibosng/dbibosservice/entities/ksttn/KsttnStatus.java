package com.ibosng.dbibosservice.entities.ksttn;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "KSTTNstatus")
@Data
public class KsttnStatus {
    @EmbeddedId
    private KsttnId id;

    @Column(name = "KSTTNkyindex")
    private Integer ksttnkyindex;

    @Column(name = "KSTTNkuerzel")
    private String ksttnkuerzel;

    @Column(name = "KSTTNbez")
    private String ksttnbez;

    @Column(name = "KSTTNloek", columnDefinition = "enum('y', 'n') default 'n'")
    private String ksttnloek;

    @Column(name = "DPWCode")
    private String dpwCode;

    @Column(name = "KSTTNaeda")
    private LocalDateTime ksttnaeda;

    @Column(name = "KSTTNaeuser", columnDefinition = "char(35)")
    private String ksttnaeuser;

    @Column(name = "KSTTNerda")
    private LocalDateTime ksttnerda;

    @Column(name = "KSTTNeruser", columnDefinition = "char(35)")
    private String ksttnEruser;
}
