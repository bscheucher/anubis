package com.ibosng.dbservice.entities.mitarbeiter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Table(name = "gehalt_info_zulage")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GehaltInfoZulage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "gehalt_info_id", referencedColumnName = "id")
    private GehaltInfo gehaltInfo;

    @Column(name = "zulage_in_euro")
    private BigDecimal zulageInEuro;

    @Column(name = "art_der_zulage")
    private String artDerZulage;

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
        return "GehaltInfoZulage{" +
                "id=" + id +
                ", gehaltInfo=" + gehaltInfo +
                ", zulageInEuro=" + zulageInEuro +
                ", artDerZulage='" + artDerZulage + '\'' +
                ", createdOn=" + createdOn +
                ", changedOn=" + changedOn +
                ", createdBy='" + createdBy + '\'' +
                ", changedBy='" + changedBy + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GehaltInfoZulage that = (GehaltInfoZulage) o;
        return id.equals(that.id) && gehaltInfo.equals(that.gehaltInfo) && Objects.equals(zulageInEuro, that.zulageInEuro) && Objects.equals(artDerZulage, that.artDerZulage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, gehaltInfo, zulageInEuro, artDerZulage);
    }
}
