package com.ibosng.dbservice.entities.rollen;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "rollen")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rollen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "rollen", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Rollen2Funktionen> rollen2Funktionens;

    @OneToMany(mappedBy = "rollen", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    List<Rollengruppen2Rolle> rollengruppen2Rolles;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

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

    public Rollen(Integer id, String name, String description, Status status, LocalDateTime createdOn, String createdBy, String changedBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.changedBy = changedBy;
    }
}
