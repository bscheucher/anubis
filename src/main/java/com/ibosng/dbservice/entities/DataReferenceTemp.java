package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "data_reference_temp")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataReferenceTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "data1")
    private String data1;

    @Column(name = "reference")
    private String reference;

    @Column(name = "data2")
    private String data2;

    @Column(name = "data3")
    private String data3;

    @Column(name = "data4")
    private String data4;

    @Column(name = "data5")
    private String data5;

    @Column(name = "data6")
    private String data6;

    @Column(name = "data7")
    private String data7;

    @Column(name = "data8")
    private String data8;

    @Column(name = "data9")
    private String data9;

    @Column(name = "data10")
    private String data10;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;


}
