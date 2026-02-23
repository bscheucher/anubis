package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.lhr.Gehaltsinfo;
import com.ibosng.dbservice.entities.masterdata.Jobticket;
import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "gehalt_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GehaltInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "vertragsdaten_id", referencedColumnName = "id")
    private Vertragsdaten vertragsdaten;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verwendungsgruppe", referencedColumnName = "id")
    private Verwendungsgruppe verwendungsgruppe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "stufe", referencedColumnName = "id")
    private KVStufe stufe;

    @Column(name = "facheinschlaegige_taetigkeiten_geprueft")
    private Boolean facheinschlaegigeTaetigkeitenGeprueft;

    @Column(name = "angerechnete_ibis_monate")
    private Integer angerechneteIbisMonate;

    @Column(name = "angerechnete_facheinschlaegige_taetigkeiten_monate")
    private Integer angerechneteFacheinschlaegigeTaetigkeitenMonate;

    @Column(name = "angerechnete_freie_taetigkeiten_monate")
    private Integer angerechneteFreieTaetigkeitenMonate;

    @Column(name = "naechste_stufe_datum")
    private LocalDate naechsteStufeDatum;

    @Column(name = "kv_gehalt_berechnet")
    private BigDecimal kvGehaltBerechnet;

    @Column(name = "gehalt_vereinbart")
    private BigDecimal gehaltVereinbart;

    private BigDecimal ueberzahlung;

    @Column(name = "gesamt_brutto")
    private BigDecimal gesamtBrutto;

    @Column(name = "vereinbarung_ueberstunden")
    private String vereinbarungUEberstunden;

    @Column(name = "uest_pauschale")
    private BigDecimal uestPauschale;

    private String deckungspruefung;

    @ManyToOne
    @JoinColumn(name = "jobticket", referencedColumnName = "id")
    private Jobticket jobticket;

    @Column(name = "lehrjahr")
    private Integer lehrjahr;

    @Column(name = "notiz_gehalt")
    private String notizGehalt;

    @Column(name = "naechste_vorrueckung")
    private LocalDate naechsteVorrueckung;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lhr_gehaltsinfo", referencedColumnName = "id")
    private Gehaltsinfo lhrGehaltsinfo;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MitarbeiterStatus status;

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
        return "GehaltInfo{" +
                "vertragsdaten=" + vertragsdaten != null ? String.valueOf(vertragsdaten.getId()) : "" +
                ", verwendungsgruppe='" + verwendungsgruppe != null ? String.valueOf(verwendungsgruppe.getId()) : "" + '\'' +
                ", stufe='" + stufe != null ? String.valueOf(stufe.getId()) : "" + '\'' +
                ", facheinschlaegigeTaetigkeitenGeprueft=" + facheinschlaegigeTaetigkeitenGeprueft +
                ", angerechneteIbisMonate=" + angerechneteIbisMonate +
                ", angerechneteFacheinschlaegigeTaetigkeitenJahre=" + angerechneteFacheinschlaegigeTaetigkeitenMonate +
                ", kvGehaltBerechnet=" + kvGehaltBerechnet +
                ", gehaltVereinbart=" + gehaltVereinbart +
                ", ueberzahlung=" + ueberzahlung +
                ", gesamtBrutto=" + gesamtBrutto +
                ", vereinbarungUEberstunden='" + vereinbarungUEberstunden + '\'' +
                ", uestPauschale=" + uestPauschale +
                ", deckungspruefung='" + deckungspruefung + '\'' +
                ", jobticket=" + jobticket +
                ", notizGehalt='" + notizGehalt + '\'' +
                ", naechsteVorrueckung='" + naechsteVorrueckung + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        GehaltInfo that = (GehaltInfo) object;
        return Objects.equals(vertragsdaten != null ? vertragsdaten.getId() : null, that.vertragsdaten != null ? that.vertragsdaten.getId() : null) &&
                Objects.equals(verwendungsgruppe != null ? verwendungsgruppe.getId() : null, that.verwendungsgruppe != null ? that.verwendungsgruppe.getId() : null) &&
                Objects.equals(stufe != null ? stufe.getId() : null, that.stufe != null ? that.stufe.getId() : null) &&
                Objects.equals(facheinschlaegigeTaetigkeitenGeprueft, that.facheinschlaegigeTaetigkeitenGeprueft) &&
                Objects.equals(angerechneteIbisMonate, that.angerechneteIbisMonate) &&
                Objects.equals(angerechneteFacheinschlaegigeTaetigkeitenMonate, that.angerechneteFacheinschlaegigeTaetigkeitenMonate) &&
                Objects.equals(angerechneteFreieTaetigkeitenMonate, that.angerechneteFreieTaetigkeitenMonate) &&
                Objects.equals(naechsteStufeDatum, that.naechsteStufeDatum) &&
                Objects.equals(kvGehaltBerechnet, that.kvGehaltBerechnet) &&
                Objects.equals(gehaltVereinbart, that.gehaltVereinbart) &&
                Objects.equals(ueberzahlung, that.ueberzahlung) &&
                Objects.equals(gesamtBrutto, that.gesamtBrutto) &&
                Objects.equals(vereinbarungUEberstunden, that.vereinbarungUEberstunden) &&
                Objects.equals(uestPauschale, that.uestPauschale) &&
                Objects.equals(deckungspruefung, that.deckungspruefung) &&
                Objects.equals(jobticket, that.jobticket) &&
                Objects.equals(notizGehalt, that.notizGehalt) &&
                Objects.equals(naechsteVorrueckung, that.naechsteVorrueckung);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertragsdaten != null ? vertragsdaten.getId() : null,
                verwendungsgruppe != null ? verwendungsgruppe.getId() : null,
                stufe != null ? stufe.getId() : null,
                facheinschlaegigeTaetigkeitenGeprueft,
                angerechneteIbisMonate,
                angerechneteFacheinschlaegigeTaetigkeitenMonate,
                angerechneteFreieTaetigkeitenMonate,
                naechsteStufeDatum,
                kvGehaltBerechnet,
                gehaltVereinbart,
                ueberzahlung,
                gesamtBrutto,
                vereinbarungUEberstunden,
                uestPauschale,
                deckungspruefung,
                jobticket,
                notizGehalt,
                naechsteVorrueckung);
    }
}

