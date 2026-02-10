package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "land")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Land {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "land_name")
    private String landName;

    @Column(name = "land_code", length = 2)
    private String landCode;

    @Column(name = "elda_code", length = 10)
    private String eldaCode;

    @Column(name = "lhr_kz", length = 10)
    private String lhrKz;

    @Column(name = "pal_kz", length = 10)
    private String palKz;

    @Column(name = "telefonvorwahl", length = 10)
    private String telefonvorwahl;

    @Column(name = "is_in_eu_eea_ch")
    private Boolean isInEuEeaCh;

    @Column(name = "is_in_sepa")
    private Boolean isInSepa;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    public Land(String landName, String landCode, String telefonvorwahl, String createdBy) {
        this.landName = landName;
        this.landCode = landCode;
        this.telefonvorwahl = telefonvorwahl;
        this.createdBy = createdBy;
    }
}
