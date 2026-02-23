package com.ibosng.dbservice.entities.lhr;

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
@Table(name = "erreichbarkeit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Erreichbarkeit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "kz")
    private String kz;

    @Column(name = "erreichbarkeitsart")
    private String erreichbarkeitsart;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;
}
