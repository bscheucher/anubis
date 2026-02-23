package com.ibosng.dbservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.moxis.UserClassifier;
import com.ibosng.dbservice.entities.rollen.Benutzer2Funktion;
import com.ibosng.dbservice.entities.rollen.Benutzer2Rolle;
import com.ibosng.dbservice.entities.rollen.Benutzer2Rollengruppe;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "benutzer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Benutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "azure_id")
    private String azureId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "upn")
    private String upn;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "personalnummer", referencedColumnName = "id")
    private Personalnummer personalnummer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "moxis_classifier")
    private UserClassifier moxisClassifier;

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

    @OneToMany(mappedBy = "benutzer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Dashboards> dashboards = new ArrayList<>();

    @OneToMany(mappedBy = "benutzer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Benutzer2Rolle> benutzer2Rolles = new ArrayList<>();

    @OneToMany(mappedBy = "benutzer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Benutzer2Funktion> benutzer2Funktionen = new ArrayList<>();

    @OneToMany(mappedBy = "benutzer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Benutzer2Rollengruppe> benutzer2Rollengruppen = new ArrayList<>();

    public String getFullName() {
        return Stream.of(firstName, lastName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" "));
    }

    @Override
    public String toString() {
        return "Benutzer{" +
                "id=" + id +
                ", azureId=" + azureId +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }
}
