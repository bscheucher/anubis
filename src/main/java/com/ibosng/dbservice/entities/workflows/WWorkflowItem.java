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
@Table(name = "w_workflow_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WWorkflowItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow_item", referencedColumnName = "id")
    private SWorkflowItem workflowItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow", referencedColumnName = "id")
    private WWorkflow workflow;

    @Column(name = "data")
    private String data;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private WWorkflowStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "predecessor", referencedColumnName = "id")
    private WWorkflowItem predecessor;

    @OneToOne
    @JoinColumn(name = "successor", referencedColumnName = "id")
    private WWorkflowItem successor;

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
        return "WWorkflowItem{" +
                "id=" + id +
                ", workflowItem=" + workflowItem.getId() +
                ", workflow=" + workflow.getId() +
                ", data='" + data + '\'' +
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
        WWorkflowItem that = (WWorkflowItem) object;
        return Objects.equals(id, that.id) &&
                Objects.equals(workflowItem.getId(), that.workflowItem.getId()) &&
                Objects.equals(workflow.getId(), that.workflow.getId()) &&
                Objects.equals(data, that.data) &&
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
        return Objects.hash(id, workflowItem.getId(), workflow.getId(), data, status, predecessor.getId(), successor.getId(), createdOn, createdBy, changedOn, changedBy);
    }
}
