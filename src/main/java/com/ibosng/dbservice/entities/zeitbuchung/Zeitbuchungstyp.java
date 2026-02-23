package com.ibosng.dbservice.entities.zeitbuchung;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Data
@Entity
@Table(name = "zeitbuchungstyp")
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Zeitbuchungstyp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "lhr_kz")
    private String lhrKz;

    @Column(name = "lhr_zeitspeicher")
    private Integer lhrZeitspeicher;

    @Column(name = "is_lhr_eintritt")
    private Boolean isLhrEintritt;

    @Column(name = "created_on")
    @Builder.Default
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Column(name = "changed_by")
    private String changedBy;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zeitbuchungstyp that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type);
    }

}


