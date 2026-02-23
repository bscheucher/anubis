package com.ibosng.dbibosservice.entities.mitarbeiter;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ARBEITSVERTRAG_ZUSATZ_FIX_KAT_CHECKED")
@Data
public class ArbeitsvertragZusatzFixKatCheckedIbos {

    @Embeddable
    @Data
    public static class ArbeitsvertragZusatzFixKatCheckedId implements Serializable {

        @Column(name = "ARBEITSVERTRAG_ZUSATZ_id", nullable = false)
        private Integer arbeitsvertragZusatzId;

        @Column(name = "kategorie_id", nullable = false)
        private Integer kategorieId;

        // Overriding equals() and hashCode() for proper composite key handling
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ArbeitsvertragZusatzFixKatCheckedId that = (ArbeitsvertragZusatzFixKatCheckedId) o;
            return Objects.equals(arbeitsvertragZusatzId, that.arbeitsvertragZusatzId) &&
                    Objects.equals(kategorieId, that.kategorieId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(arbeitsvertragZusatzId, kategorieId);
        }
    }

    @EmbeddedId
    private ArbeitsvertragZusatzFixKatCheckedId id;
}

