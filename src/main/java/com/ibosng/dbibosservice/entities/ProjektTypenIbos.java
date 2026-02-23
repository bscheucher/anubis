package com.ibosng.dbibosservice.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "PROJEKTTYPEN")
public class ProjektTypenIbos {

        @Id
        @Column(name = "id", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "bezeichnung", columnDefinition = "char(50)")
        private String bezeichnung;

        @Column(name = "aktiv", columnDefinition = "TINYINT(1) DEFAULT 1")
        private Boolean aktiv;
}
