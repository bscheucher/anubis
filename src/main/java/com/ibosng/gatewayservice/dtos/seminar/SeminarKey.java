package com.ibosng.gatewayservice.dtos.seminar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeminarKey {
    private int seminarNummer;
    private String name;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        SeminarKey that = (SeminarKey) object;
        return seminarNummer == that.seminarNummer && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seminarNummer, name);
    }
}
