package com.ibosng.validationservice.utils;

import com.ibosng.BaseIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParsersTest extends BaseIntegrationTest {

    @Test
    public void whenParseValidLongString_thenReturnLong() {
        String validLong = "1234567890";
        Long result = Parsers.parseStringToLong(validLong);
        assertEquals(1234567890L, result);
    }

    @Test
    public void whenParseInvalidLongString_thenReturnNull() {
        String invalidLong = "abc";
        Long result = Parsers.parseStringToLong(invalidLong);
        assertNull(result);
    }

    @Test
    public void whenParseValidIntegerString_thenReturnInteger() {
        String validInteger = "12345";
        Integer result = Parsers.parseStringToInteger(validInteger);
        assertEquals(12345, result);
    }

    @Test
    public void whenParseInvalidIntegerString_thenReturnNull() {
        String invalidInteger = "abc";
        Integer result = Parsers.parseStringToInteger(invalidInteger);
        assertNull(result);
    }

    @Test
    public void testConstructor() {
        Parsers parsers = new Parsers();
        assertTrue(parsers instanceof Parsers);
    }
}