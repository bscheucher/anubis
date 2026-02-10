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
@Table(name = "labels")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Label {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "label_key")
    private String labelKey;

    @Column(name = "label_text")
    private String labelText;

    @Column(name = "section_name")
    private String sectionName;

    @ManyToOne
    @JoinColumn(name = "language_id", referencedColumnName = "id")
    private Language language;

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
    private LocalDateTime changedOn;

    @JsonIgnore
    @Column(name = "changed_by")
    private String changedBy;

    @Override
    public String toString() {
        return "Label{" +
                "id=" + id +
                ", labelKey='" + labelKey + '\'' +
                ", labelText='" + labelText + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", language=" + language != null ? String.valueOf(language.getId()) : "" +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Label label = (Label) object;
        return Objects.equals(labelKey, label.labelKey) &&
                Objects.equals(labelText, label.labelText) &&
                Objects.equals(sectionName, label.sectionName) &&
                Objects.equals(language != null ? language.getId() : null, label.language != null ? label.language.getId() : null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(labelKey, labelText, sectionName, language != null ? language.getId() : null);
    }
}
