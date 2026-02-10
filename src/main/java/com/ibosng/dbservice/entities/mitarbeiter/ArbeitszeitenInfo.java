package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.Arbeitszeitmodell;
import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsausmass;
import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsstatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "arbeitszeiten_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArbeitszeitenInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "vertragsdaten_id", referencedColumnName = "id")
    private Vertragsdaten vertragsdaten;

    private String stundenaenderung;
    private String verwendungsbereichsaenderung;
    private String stufenwechsel;
    private String geschaeftsbereichsaenderung;
    private Boolean kvErhoehung;

    @ManyToOne
    @JoinColumn(name = "beschaeftigungsausmass", referencedColumnName = "id")
    private Beschaeftigungsausmass beschaeftigungsausmass;

    @ManyToOne
    @JoinColumn(name = "beschaeftigungsstatus", referencedColumnName = "id")
    private Beschaeftigungsstatus beschaeftigungsstatus;

    private String wochenstunden;

    @ManyToOne
    @JoinColumn(name = "arbeitszeitmodell", referencedColumnName = "id")
    private Arbeitszeitmodell arbeitszeitmodell;

    @Column(name = "arbeitszeitmodell_von")
    private LocalDate arbeitszeitmodellVon;

    @Column(name = "arbeitszeitmodell_bis")
    private LocalDate arbeitszeitmodellBis;

    @Column(name = "auswahl_begruendung_fuer_durchrechner")
    private String auswahlBegruendungFuerDurchrechner;

    @Column(name = "spezielle_mittagspausenregelung")
    private String spezielleMittagspausenregelung;

    @Column(name = "urlaub_vorab_vereinbart")
    private Boolean urlaubVorabVereinbart;

    @Column(name = "notiz_arbeitszeit")
    private String notizArbeitszeit;

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
        return "ArbeitszeitenInfo{" +
                "vertragsdaten=" + vertragsdaten.getId() +
                ", stundenaenderung='" + stundenaenderung + '\'' +
                ", verwendungsbereichsaenderung='" + verwendungsbereichsaenderung + '\'' +
                ", stufenwechsel='" + stufenwechsel + '\'' +
                ", geschaeftsbereichsaenderung='" + geschaeftsbereichsaenderung + '\'' +
                ", kvErhoehung=" + kvErhoehung +
                ", beschaeftigungsausmass='" + beschaeftigungsausmass + '\'' +
                ", beschaeftigungsstatus='" + beschaeftigungsstatus + '\'' +
                ", wochenstunden='" + wochenstunden + '\'' +
                ", arbeitszeitmodell='" + arbeitszeitmodell != null ? arbeitszeitmodell.getName() : "" + '\'' +
                ", arbeitszeitmodellVon=" + arbeitszeitmodellVon +
                ", arbeitszeitmodellBis=" + arbeitszeitmodellBis +
                ", auswahlBegruendungFuerDurchrechner='" + auswahlBegruendungFuerDurchrechner + '\'' +
                ", spezielleMittagspausenregelung='" + spezielleMittagspausenregelung + '\'' +
                ", urlaubVorabVereinbart=" + urlaubVorabVereinbart +
                ", notizArbeitszeit='" + notizArbeitszeit + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ArbeitszeitenInfo that = (ArbeitszeitenInfo) object;
        return Objects.equals(vertragsdaten != null ? vertragsdaten.getId() : null, that.vertragsdaten != null ? that.vertragsdaten.getId() : null) &&
                Objects.equals(stundenaenderung, that.stundenaenderung) &&
                Objects.equals(verwendungsbereichsaenderung, that.verwendungsbereichsaenderung) &&
                Objects.equals(stufenwechsel, that.stufenwechsel) &&
                Objects.equals(geschaeftsbereichsaenderung, that.geschaeftsbereichsaenderung) &&
                Objects.equals(kvErhoehung, that.kvErhoehung) &&
                Objects.equals(beschaeftigungsausmass, that.beschaeftigungsausmass) &&
                Objects.equals(beschaeftigungsstatus, that.beschaeftigungsstatus) &&
                Objects.equals(wochenstunden, that.wochenstunden) &&
                Objects.equals(arbeitszeitmodell != null ? arbeitszeitmodell.getId() : null, that.arbeitszeitmodell != null ? that.arbeitszeitmodell.getId() : null) &&
                Objects.equals(arbeitszeitmodellVon, that.arbeitszeitmodellVon) &&
                Objects.equals(arbeitszeitmodellBis, that.arbeitszeitmodellBis) &&
                Objects.equals(auswahlBegruendungFuerDurchrechner, that.auswahlBegruendungFuerDurchrechner) &&
                Objects.equals(spezielleMittagspausenregelung, that.spezielleMittagspausenregelung) &&
                Objects.equals(urlaubVorabVereinbart, that.urlaubVorabVereinbart) &&
                Objects.equals(notizArbeitszeit, that.notizArbeitszeit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertragsdaten != null ? vertragsdaten.getId() : null, stundenaenderung, verwendungsbereichsaenderung, stufenwechsel, geschaeftsbereichsaenderung, kvErhoehung, beschaeftigungsausmass, beschaeftigungsstatus, wochenstunden, arbeitszeitmodell != null ? arbeitszeitmodell.getId() : null, arbeitszeitmodellVon, arbeitszeitmodellBis, auswahlBegruendungFuerDurchrechner, spezielleMittagspausenregelung, urlaubVorabVereinbart, notizArbeitszeit);
    }
}
