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
@Table(name = "w_workflows")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow", referencedColumnName = "id")
    private SWorkflow workflow;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow_group", referencedColumnName = "id")
    private WWorkflowGroup workflowGroup;

    @Column(name = "data")
    private String data;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private WWorkflowStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "predecessor", referencedColumnName = "id")
    private WWorkflow predecessor;

    @OneToOne
    @JoinColumn(name = "successor", referencedColumnName = "id")
    private WWorkflow successor;

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
        return "WWorkflow{" +
                "id=" + id +
                ", workflow=" + workflow != null ? String.valueOf(workflow.getId()) : "no id" +
                ", workflowGroup=" + workflowGroup != null ? String.valueOf(workflowGroup.getId()) : "no workflowgroup" +
                ", data='" + data + '\'' +
                ", status=" + status +
                ", predecessor=" + predecessor != null ? String.valueOf(predecessor.getId()) : "no predecessor" +
                ", successor=" + successor != null ? String.valueOf(successor.getId()) : "nu successor" +
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
        WWorkflow workflow1 = (WWorkflow) object;
        return  Objects.equals(workflow != null ? workflow.getId() : null, workflow1.workflow != null ? workflow1.workflow.getId() : null) &&
                Objects.equals(workflowGroup != null ? workflowGroup.getId() : null, workflow1.workflowGroup != null ? workflow1.workflowGroup.getId() : null) &&
                Objects.equals(data, workflow1.data) &&
                status == workflow1.status &&
                Objects.equals(predecessor != null ? predecessor.getId() : null, workflow1.predecessor != null ? workflow1.predecessor.getId() : null) &&
                Objects.equals(successor != null ? successor.getId() : null, workflow1.successor != null ? workflow1.successor.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(workflow != null ? workflow.getId() : null, workflowGroup != null ? workflowGroup.getId() : null, data, status, predecessor != null ? predecessor.getId() : null, successor != null ? successor.getId() : null);
    }
}
