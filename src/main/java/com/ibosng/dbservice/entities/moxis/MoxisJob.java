package com.ibosng.dbservice.entities.moxis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "moxis_job")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoxisJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "position_type")
    private PositionType positionType;

    @Column(name = "instance_id")
    private Integer instanceId;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "expiration_date")
    private OffsetDateTime expirationDate = OffsetDateTime.now();

    @Column(name = "reference_id")
    private String referenceId;

    @Column(name = "constituent")
    private String constituent;

    @OneToMany(mappedBy = "moxisJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IterationData> iterationDataList = new ArrayList<>();

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MoxisJobStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow")
    private WWorkflow workflow;

    @Enumerated(EnumType.STRING)
    @Column(name = "job_type")
    private SigningJobType signingJobType;

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
        return "MoxisJob{" +
                "id=" + id +
                ", positionType=" + positionType +
                ", instance_id=" + instanceId +
                ", description='" + description + '\'' +
                ", category=" + category +
                ", expirationDate=" + expirationDate +
                ", referenceId='" + referenceId + '\'' +
                ", constituent=" + constituent != null ? constituent : ""+
                ", iterationDataList=" + iterationDataList.stream().map(IterationData::getId).toList() +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MoxisJob moxisJob = (MoxisJob) object;
        return Objects.equals(id, moxisJob.id) &&
                positionType == moxisJob.positionType &&
                Objects.equals(instanceId, moxisJob.instanceId) &&
                Objects.equals(description, moxisJob.description) &&
                category == moxisJob.category &&
                Objects.equals(expirationDate, moxisJob.expirationDate) &&
                Objects.equals(referenceId, moxisJob.referenceId) &&
                Objects.equals(constituent != null ? constituent : null, moxisJob.constituent != null ? moxisJob.constituent : null) &&
                Objects.equals(iterationDataList.stream().map(IterationData::getId).toList(), moxisJob.iterationDataList.stream().map(IterationData::getId).toList()) &&
                status == moxisJob.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, positionType, instanceId, description, category, expirationDate, referenceId, constituent != null ? constituent : null, iterationDataList.stream().map(IterationData::getId).toList(), status);
    }
}
