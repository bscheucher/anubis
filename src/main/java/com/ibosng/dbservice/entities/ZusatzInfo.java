package com.ibosng.dbservice.entities;

import com.ibosng.dbservice.entities.lhr.Erreichbarkeit;
import com.ibosng.dbservice.entities.lhr.Religion;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "zusatz_info")
public class ZusatzInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "burgenland")
    private Boolean burgenland;

    @Column(name = "kaernten")
    private Boolean kaernten;

    @Column(name = "niederoesterreich")
    private Boolean niederoesterreich;

    @Column(name = "oberoesterreich")
    private Boolean oberoesterreich;

    @Column(name = "salzburg")
    private Boolean salzburg;

    @Column(name = "steiermark")
    private Boolean steiermark;

    @Column(name = "tirol")
    private Boolean tirol;

    @Column(name = "vorarlberg")
    private Boolean vorarlberg;

    @Column(name = "wien")
    private Boolean wien;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status")
    private MitarbeiterStatus status;

    private String arbeitsgenehmigung;

    private LocalDate gueltigBis;

    @Enumerated(EnumType.STRING)
    @Column(name = "arbeitsgenehmigung_status")
    private BlobStatus arbeitsgenehmigungStatus;

    @Enumerated(EnumType.STRING)
    private BlobStatus foto;

    @ManyToOne
    @JoinColumn(name = "religion")
    private Religion religion;

    @ManyToOne
    @JoinColumn(name = "erreichbarkeit")
    private Erreichbarkeit erreichbarkeit;

    @Column(name = "created_on")
    private LocalDateTime createdOn = getLocalDateNow();

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "changed_by")
    private String changedBy;

    @Column(name = "changed_on")
    private LocalDateTime changedOn;

    @Override
    public String toString() {
        return "ZusatzInfo{" +
                "id=" + id +
                ", burgenland=" + burgenland +
                ", kaernten=" + kaernten +
                ", niederoesterreich=" + niederoesterreich +
                ", oberoesterreich=" + oberoesterreich +
                ", salzburg=" + salzburg +
                ", steiermark=" + steiermark +
                ", tirol=" + tirol +
                ", vorarlberg=" + vorarlberg +
                ", wien=" + wien +
                ", status=" + status +
                ", arbeitsgenehmigung='" + arbeitsgenehmigung + '\'' +
                ", gueltigBis=" + gueltigBis +
                ", arbeitsgenehmigungStatus=" + arbeitsgenehmigungStatus +
                ", foto=" + foto +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ZusatzInfo that = (ZusatzInfo) object;
        return Objects.equals(burgenland, that.burgenland) &&
                Objects.equals(kaernten, that.kaernten) &&
                Objects.equals(niederoesterreich, that.niederoesterreich) &&
                Objects.equals(oberoesterreich, that.oberoesterreich) &&
                Objects.equals(salzburg, that.salzburg) &&
                Objects.equals(steiermark, that.steiermark) &&
                Objects.equals(tirol, that.tirol) &&
                Objects.equals(vorarlberg, that.vorarlberg) &&
                Objects.equals(wien, that.wien) &&
                Objects.equals(arbeitsgenehmigung, that.arbeitsgenehmigung) &&
                Objects.equals(gueltigBis, that.gueltigBis) &&
                arbeitsgenehmigungStatus == that.arbeitsgenehmigungStatus &&
                foto == that.foto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(burgenland, kaernten, niederoesterreich, oberoesterreich, salzburg, steiermark, tirol, vorarlberg, wien, arbeitsgenehmigung, gueltigBis, arbeitsgenehmigungStatus, foto);
    }
}
