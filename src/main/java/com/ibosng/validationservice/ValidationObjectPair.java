package com.ibosng.validationservice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class ValidationObjectPair<T, V> {
    private final T first;
    private final V second;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationObjectPair<?, ?> pairKey = (ValidationObjectPair<?, ?>) o;
        return Objects.equals(first, pairKey.first) &&
                Objects.equals(second, pairKey.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}
