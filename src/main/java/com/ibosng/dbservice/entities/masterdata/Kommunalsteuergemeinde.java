package com.ibosng.dbservice.entities.masterdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ibosng.dbservice.entities.Land;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "kommunalsteuergemeinde")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kommunalsteuergemeinde {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "dienstort_plz")
    private Integer dienstortPlz;

    @ManyToOne
    @JoinColumn(name = "lhr_land")
    private Land lhrLand;

    @Column(name = "lhr_plz")
    private Integer lhrPlz;

    @Column(name = "lhr_name")
    private String lhrName;

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @JsonIgnore
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "changed_on")
    private LocalDateTime changedOn = getLocalDateNow();

    @JsonIgnore
    @Column(name = "created_by")
    private String createdBy;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Kommunalsteuergemeinde{" +
                "id=" + id +
                ", dienstortPlz=" + dienstortPlz +
                ", lhrLand=" + lhrLand != null ? String.valueOf(lhrLand.getId()) : " " +
                ", lhrPlz=" + lhrPlz +
                ", lhrName='" + lhrName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Kommunalsteuergemeinde that = (Kommunalsteuergemeinde) object;
        return Objects.equals(dienstortPlz, that.dienstortPlz) &&
                Objects.equals(lhrLand != null ? lhrLand.getId() : null, that.lhrLand != null ? that.lhrLand.getId() : null) &&
                Objects.equals(lhrPlz, that.lhrPlz) &&
                Objects.equals(lhrName, that.lhrName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dienstortPlz, lhrLand != null ? lhrLand.getId() : null, lhrPlz, lhrName);
    }
}
