package com.ibosng.dbservice.entities.teilnehmer;

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
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teilnehmer_data_status")
public class TeilnehmerDataStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "teilnehmer", referencedColumnName = "id")
    protected Teilnehmer teilnehmer;

    @Column(name = "error")
    private String error;

    @Column(name = "cause")
    private String cause;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeilnehmerDataStatus that = (TeilnehmerDataStatus) o;
        return Objects.equals(teilnehmer != null ? teilnehmer.getId() : null, that.teilnehmer != null ? that.teilnehmer.getId() : null) &&
                Objects.equals(error, that.error) && Objects.equals(cause, that.cause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, cause, teilnehmer != null ? teilnehmer.getId() : null);
    }

    @Override
    public String toString() {
        return "TeilnehmerDataStatus{" +
                ", error='" + error + '\'' +
                ", cause='" + cause + '\'' +
                '}';
    }
}
