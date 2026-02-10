package com.ibosng.dbservice.entities.moxis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "moxis_iteration_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IterationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "category")
    private String category;

    @Column(name = "iteration_number")
    private Integer iterationNumber;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "job_id")
    private MoxisJob moxisJob;

    @OneToMany(mappedBy = "iterationData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invitee> invitees = new ArrayList<>();

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
        return "IterationData{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", iterationNumber=" + iterationNumber +
                ", moxisJob=" + moxisJob != null ? String.valueOf(moxisJob.getId()) : "" +
                ", invitees=" + invitees.stream().map(Invitee::getId).toList() +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        IterationData that = (IterationData) object;
        return Objects.equals(category, that.category) &&
                Objects.equals(iterationNumber, that.iterationNumber) &&
                Objects.equals(moxisJob != null ? moxisJob.getId() : null, that.moxisJob != null ? that.moxisJob.getId() : null) &&
                Objects.equals(invitees.stream().map(Invitee::getId).toList(), that.invitees.stream().map(Invitee::getId).toList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, iterationNumber, moxisJob != null ? moxisJob.getId() : null, invitees.stream().map(Invitee::getId).toList());
    }
}
