package com.ibosng.dbservice.entities.moxis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "moxis_invitee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invitee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "classifier")
    private UserClassifier classifier;

    @Column(name = "name")
    private String name;

    @Column(name = "role_name")
    private String roleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "class")
    private UserClass userClass;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "signature_position")
    private DirectSignaturePosition directUserPosition;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "iteration_data")
    private IterationData iterationData;

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

    @Override
    public String toString() {
        return "Invitee{" +
                "id=" + id +
                ", classifier=" + classifier  +
                ", name='" + name + '\'' +
                ", roleName='" + roleName + '\'' +
                ", userClass=" + userClass +
                ", directUserPosition=" + directUserPosition != null ? String.valueOf(directUserPosition.getId()) : "" +
                ", iterationData=" + iterationData != null ? String.valueOf(iterationData.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Invitee invitee = (Invitee) object;
        return classifier == invitee.classifier &&
                Objects.equals(name, invitee.name) &&
                Objects.equals(roleName, invitee.roleName) &&
                userClass == invitee.userClass &&
                Objects.equals(directUserPosition != null ? directUserPosition.getId() : null, invitee.directUserPosition != null ? invitee.directUserPosition.getId() : null) &&
                Objects.equals(iterationData != null ? iterationData.getId() : null, invitee.iterationData != null ? invitee.iterationData.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(classifier, name, roleName, userClass, directUserPosition != null ? directUserPosition.getId() : null, iterationData != null ? iterationData.getId() : null);
    }
}
