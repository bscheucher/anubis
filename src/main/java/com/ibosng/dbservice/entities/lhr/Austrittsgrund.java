package com.ibosng.dbservice.entities.lhr;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "austrittsgrund")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Austrittsgrund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "lhr_grund")
    private String lhrGrund;

    @Column(name = "beschreibung")
    private String beschreibung;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;


}
