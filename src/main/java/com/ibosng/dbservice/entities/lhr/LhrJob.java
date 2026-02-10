package com.ibosng.dbservice.entities.lhr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.moxis.SigningJobType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "lhr_job")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LhrJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "eintritt")
    private LocalDate eintritt;

    @ManyToOne
    @JoinColumn(name = "personalnummer")
    private Personalnummer personalnummer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private LhrJobStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private SigningJobType jobType;

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
