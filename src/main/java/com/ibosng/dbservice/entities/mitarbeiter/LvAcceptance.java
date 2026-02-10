package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "lv_acceptance")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LvAcceptance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @Column(name = "bankcard")
    private boolean bankcard = false;

    @Column(name = "bankcardReason")
    private String bankcardReason;

    @Column(name = "ecard")
    private boolean ecard = false;

    @Column(name = "ecardReason")
    private String ecardReason;

    @Column(name = "arbeitsgenehmigungDok")
    private boolean arbeitsgenehmigungDok = false;

    @Column(name = "arbeitsgenehmigungDokReason")
    private String arbeitsgenehmigungDokReason;

    @Column(name = "gehaltEinstufung")
    private boolean gehaltEinstufung = false;

    @Column(name = "gehaltEinstufungReason")
    private String gehaltEinstufungReason;

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
        return "LvAcceptance{" +
                "id=" + id +
                ", vertragsdaten=" + personalnummer != null ? personalnummer.getPersonalnummer() : "" +
                ", bankcard=" + bankcard +
                ", bankcardReason='" + bankcardReason + '\'' +
                ", ecard=" + ecard +
                ", ecardReason='" + ecardReason + '\'' +
                ", arbeitsgenehmigungDok=" + arbeitsgenehmigungDok +
                ", arbeitsgenehmigungDokReason='" + arbeitsgenehmigungDokReason + '\'' +
                ", gehaltEinstufung=" + gehaltEinstufung +
                ", gehaltEinstufungReason='" + gehaltEinstufungReason + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        LvAcceptance that = (LvAcceptance) object;
        return Objects.equals(personalnummer != null ? personalnummer.getId() : null, that.personalnummer != null ? that.personalnummer.getId() : null) &&
                Objects.equals(bankcard, that.bankcard) &&
                Objects.equals(bankcardReason, that.bankcardReason) &&
                Objects.equals(ecard, that.ecard) &&
                Objects.equals(ecardReason, that.ecardReason) &&
                Objects.equals(arbeitsgenehmigungDok, that.arbeitsgenehmigungDok) &&
                Objects.equals(arbeitsgenehmigungDokReason, that.arbeitsgenehmigungDokReason) &&
                Objects.equals(gehaltEinstufung, that.gehaltEinstufung) &&
                Objects.equals(gehaltEinstufungReason, that.gehaltEinstufungReason) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(personalnummer != null ? personalnummer.getId() : null, bankcard, bankcardReason, ecard, ecardReason, arbeitsgenehmigungDok, arbeitsgenehmigungDokReason, gehaltEinstufung, gehaltEinstufungReason, status);
    }
}

