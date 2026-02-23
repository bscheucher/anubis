package com.ibosng.dbservice.entities.masterdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Status;
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
@Table(name = "gehalt")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Gehalt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "gehalt")
    private BigDecimal gehalt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "kv_stufe")
    private KVStufe kvStufe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "verwendungsgruppe")
    private Verwendungsgruppe verwendungsgruppe;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @Column(name = "gueltig_ab")
    private LocalDate gueltigAb;

    @Column(name = "gueltig_bis")
    private LocalDate gueltigBis;

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
        return "Gehalt{" +
                "id=" + id +
                ", gehalt=" + gehalt +
                ", kvStufe=" + kvStufe != null ? String.valueOf(kvStufe.getId()) : "" +
                ", verwendungsgruppe=" + verwendungsgruppe != null ? String.valueOf(verwendungsgruppe.getId()) : "" +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Gehalt gehalt1 = (Gehalt) object;
        return Objects.equals(gehalt, gehalt1.gehalt) &&
                Objects.equals(kvStufe != null ? kvStufe.getId() : null, gehalt1.kvStufe != null ? gehalt1.kvStufe.getId() : null) &&
                Objects.equals(verwendungsgruppe != null ? verwendungsgruppe.getId() : null, gehalt1.verwendungsgruppe != null ? gehalt1.verwendungsgruppe.getId() : null) &&
                status == gehalt1.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gehalt, kvStufe != null ? kvStufe.getId() : null, verwendungsgruppe != null ? verwendungsgruppe.getId() : null, status);
    }
}
