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
@Table(name = "signature_position")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DirectSignaturePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "x")
    private String x;

    @Column(name = "y")
    private String y;

    @Column(name = "width")
    private String width;

    @Column(name = "height")
    private String height;

    @Column(name = "page_number")
    private Integer pageNumber;

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
        return "DirectSignaturePosition{" +
                "id=" + id +
                ", x='" + x + '\'' +
                ", y='" + y + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", pageNumber=" + pageNumber +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        DirectSignaturePosition that = (DirectSignaturePosition) object;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y) && Objects.equals(width, that.width) && Objects.equals(height, that.height) && Objects.equals(pageNumber, that.pageNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height, pageNumber);
    }
}
