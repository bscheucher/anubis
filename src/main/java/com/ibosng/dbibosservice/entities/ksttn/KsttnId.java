package com.ibosng.dbibosservice.entities.ksttn;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class KsttnId {
    @Column(name = "KSTTNkynr")
    private Integer ksttnkynr;

    @Column(name = "TEILNAHMESTATUS_BEREICH_id")
    private Integer teilnahmestatusBereichId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KsttnId ksttnId)) return false;
        return Objects.equals(getKsttnkynr(), ksttnId.getKsttnkynr()) && Objects.equals(getTeilnahmestatusBereichId(), ksttnId.getTeilnahmestatusBereichId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKsttnkynr(), getTeilnahmestatusBereichId());
    }
}
