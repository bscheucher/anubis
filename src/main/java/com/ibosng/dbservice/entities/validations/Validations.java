package com.ibosng.dbservice.entities.validations;

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
@Table(name = "validations")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Validations {

    public Validations(ValidationType type, ValidationStatus status, String message, String identifier, String createdBy) {
        this(type, status, message, identifier, null, createdBy);
    }

    public Validations(ValidationType type, ValidationStatus status, String message, String identifier, Integer entityId, String createdBy) {
        this.type = type;
        this.status = status;
        this.message = message;
        this.identifier = identifier;
        this.createdBy = createdBy;
        this.entityId = entityId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private ValidationType type;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private ValidationStatus status;

    @Column(name = "message")
    private String message;

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "entity_id")
    private Integer entityId;

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
}
