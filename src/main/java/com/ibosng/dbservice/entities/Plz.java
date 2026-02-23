package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "plz")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plz extends BasePlz {

    @Column(name = "plz")
    private Integer plz;

    @Column(name = "ort")
    private String ort;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bundesland")
    private Bundesland bundesland;

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

    @Override
    public String getPlzString() {
        return String.valueOf(plz);
    }
}
