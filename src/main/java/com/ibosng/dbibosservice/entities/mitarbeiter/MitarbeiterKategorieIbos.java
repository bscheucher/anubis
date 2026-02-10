package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MITARBEITER_KATEGORIE")
@Data
public class MitarbeiterKategorieIbos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "bezeichnung", nullable = false, unique = true, length = 100)
    private String bezeichnung;

    @Column(name = "tc_bezeichnung", nullable = false, length = 20)
    private String tcBezeichnung;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "geburtstagsmail", nullable = true, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean geburtstagsmail;
}