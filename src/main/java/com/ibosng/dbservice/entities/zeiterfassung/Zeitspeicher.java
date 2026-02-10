package com.ibosng.dbservice.entities.zeiterfassung;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "zeitspeicher")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Zeitspeicher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "zeitspeicher_nummer")
    Integer zeitspeicherNummer;

    @Column(name = "name")
    String name;

    @Column(name = "abbreviation")
    String abbreviation;

    @Column(name = "comment")
    String comment;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;
}
