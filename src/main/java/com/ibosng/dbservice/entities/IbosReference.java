package com.ibosng.dbservice.entities;

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
@Table(name = "ibos_reference")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IbosReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "ibosng_id")
    private Integer ibosngId;

    @Column(name = "ibos_id")
    private Integer ibosId;

    @Column(name = "data")
    private String data;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private Status status;

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
        return "IbosReference{" +
                "ibosngId=" + ibosngId +
                ", ibosId=" + ibosId +
                ", data='" + data + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        IbosReference that = (IbosReference) object;
        return Objects.equals(ibosngId, that.ibosngId) && Objects.equals(ibosId, that.ibosId) && Objects.equals(data, that.data) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ibosngId, ibosId, data, status);
    }
}
