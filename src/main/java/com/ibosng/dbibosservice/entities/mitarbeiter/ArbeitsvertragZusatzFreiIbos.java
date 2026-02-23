package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "ARBEITSVERTRAG_ZUSATZ_FREI")
@Data
public class ArbeitsvertragZusatzFreiIbos {

    @Id
    @Column(name = "ARBEITSVERTRAG_ZUSATZ_id", nullable = false, updatable = false)
    private Integer arbeitsvertragZusatzId;

    @Column(name = "stdsatz", precision = 9, scale = 2, columnDefinition = "DECIMAL(9, 2)")
    private BigDecimal stdsatz;

    @Column(name = "max_arbeitsstd", precision = 5, scale = 2, columnDefinition = "DECIMAL(5, 2)")
    private BigDecimal maxArbeitsstd;

    @Column(name = "arbeitstage", columnDefinition = "INT(6) UNSIGNED")
    private Integer arbeitstage;
}
