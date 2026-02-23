package com.ibosng.validationservice;

import com.ibosng.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationObjectPairTest extends BaseIntegrationTest {

    @Test
    public void testEqualsWithEqualPairs() {
        ValidationObjectPair<String, Integer> pair1 = new ValidationObjectPair<>("Key", 1);
        ValidationObjectPair<String, Integer> pair2 = new ValidationObjectPair<>("Key", 1);

        assertEquals(pair1, pair2);
    }

    @Test
    public void testEqualsWithUnequalPairs() {
        ValidationObjectPair<String, Integer> pair1 = new ValidationObjectPair<>("Key1", 1);
        ValidationObjectPair<String, Integer> pair2 = new ValidationObjectPair<>("Key2", 2);

        assertNotEquals(pair1, pair2);
    }

    @Test
    public void testHashCodeWithEqualPairs() {
        ValidationObjectPair<String, Integer> pair1 = new ValidationObjectPair<>("Key", 1);
        ValidationObjectPair<String, Integer> pair2 = new ValidationObjectPair<>("Key", 1);

        assertEquals(pair1.hashCode(), pair2.hashCode());
    }

    @Test
    public void testHashCodeWithUnequalPairs() {
        ValidationObjectPair<String, Integer> pair1 = new ValidationObjectPair<>("Key1", 1);
        ValidationObjectPair<String, Integer> pair2 = new ValidationObjectPair<>("Key2", 2);

        assertNotEquals(pair1.hashCode(), pair2.hashCode());
    }

    @Test
    public void testGetFirstAndSecond() {
        ValidationObjectPair<String, Integer> pair1 = new ValidationObjectPair<>("Key", 1);
        String first = pair1.getFirst();
        Integer second = pair1.getSecond();
        assertEquals("Key", first);
        assertEquals(1, second);
    }

    @Test
    public void testToString() {
        ValidationObjectPair<String, Integer> pair1 = new ValidationObjectPair<>("Key", 1);
        assertNotNull(pair1.toString());
    }
}
