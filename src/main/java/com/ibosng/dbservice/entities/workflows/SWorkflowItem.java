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
@Table(name = "s_workflow_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SWorkflowItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow", referencedColumnName = "id")
    private SWorkflow workflow;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private SWorkflowStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "predecessor", referencedColumnName = "id")
    private SWorkflowItem predecessor;

    @OneToOne
    @JoinColumn(name = "successor", referencedColumnName = "id")
    private SWorkflowItem successor;

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
        return "SWorkflowItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", workflow=" + workflow.getId() +
                ", status=" + status +
                ", predecessor=" + predecessor.getId() +
                ", successor=" + successor.getId() +
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
        SWorkflowItem that = (SWorkflowItem) object;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(workflow.getId(), that.workflow.getId()) &&
                status == that.status &&
                Objects.equals(predecessor.getId(), that.predecessor.getId()) &&
                Objects.equals(successor.getId(), that.successor.getId()) &&
                Objects.equals(createdOn, that.createdOn) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(changedOn, that.changedOn) &&
                Objects.equals(changedBy, that.changedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, workflow.getId(), status, predecessor.getId(), successor.getId(), createdOn, createdBy, changedOn, changedBy);
    }
}
