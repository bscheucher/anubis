package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Land;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "bank_daten")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDaten {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "bank")
    private String bank;

    @Column(name = "iban")
    private String iban;

    @Column(name = "bic")
    private String bic;

    @Column(name = "blz")
    private String blz;

    @ManyToOne
    @JoinColumn(name = "land")
    private Land land;

    @Column(name = "lautend_auf")
    private String lautendAuf;

    @ManyToOne
    @JoinColumn(name = "auftraggeber_bank")
    private AuftraggeberBank auftraggeberBank;

    @Column(name = "bar_bei_austritt", nullable = false)
    private boolean barBeiAustritt = false;

    @Column(name = "card_status")
    @Enumerated(EnumType.STRING)
    private BlobStatus card;

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
        return "BankDaten{" + ", bank='" + bank + '\'' + ", iban='" + iban + '\'' + ", bic='" + bic + '\'' + ", card='" + card + '\'' + ", status='" + status + '\'' + '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        BankDaten bankDaten = (BankDaten) object;
        return Objects.equals(bank, bankDaten.bank) && Objects.equals(iban, bankDaten.iban) && Objects.equals(bic, bankDaten.bic) && Objects.equals(card, bankDaten.card) && status == bankDaten.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bank, iban, bic, card, status);
    }
}
