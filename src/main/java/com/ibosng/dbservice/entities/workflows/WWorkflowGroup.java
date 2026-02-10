package com.ibosng.dbservice.entities.workflows;

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
@Table(name = "w_workflow_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WWorkflowGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow_group", referencedColumnName = "id")
    private SWorkflowGroup workflowGroup;

    @Column(name = "data")
    private String data;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private WWorkflowStatus status;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "WWorkflowGroup{" +
                "id=" + id +
                ", workflowGroup=" + workflowGroup != null ? String.valueOf(workflowGroup.getId()) : "no workflowgroup" +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedOn=" + changedOn +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        WWorkflowGroup that = (WWorkflowGroup) object;
        return Objects.equals(id, that.id) &&
                Objects.equals(workflowGroup != null ? workflowGroup.getId() : null, that.workflowGroup != null ? that.workflowGroup.getId() : null) &&
                Objects.equals(data, that.data) &&
                status == that.status &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(changedOn, that.changedOn) &&
                Objects.equals(changedBy, that.changedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workflowGroup != null ? workflowGroup.getId() : null, data, status, createdOn, createdBy, changedOn, changedBy);
    }
}
