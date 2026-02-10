package com.ibosng.dbservice.entities;

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
@Table(name = "widgets_in_dashboards")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WidgetsInDashboards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "widget_id")
    private Widget widgetId;

    @Column(name = "position_x")
    private Integer positionX;

    @Column(name = "position_y")
    private Integer positionY;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status Status;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dashboard_id")
    private Dashboards dashboard;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WidgetsInDashboards that = (WidgetsInDashboards) o;
        return Objects.equals(widgetId.getId(), that.widgetId.getId()) && Objects.equals(positionX, that.positionX) && Objects.equals(positionY, that.positionY) && Status == that.Status && Objects.equals(dashboard.getId(), that.dashboard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, widgetId.getId(), positionX, positionY, Status, dashboard.getId());
    }

    @Override
    public String toString() {
        return "WidgetsInDashboards{" +
                "id=" + id +
                ", widgetId=" + widgetId.getId() +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                ", Status=" + Status +
                ", createdOn=" + createdOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedBy='" + changedBy + '\'' +
                ", dashboard=" + dashboard.getId() +
                '}';
    }
}
